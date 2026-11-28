package com.itera.pam.p7.latihan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.itera.pam.p7.db.AppDatabase
import com.itera.pam.p7.db.Draf
import com.itera.pam.p7.db.Laporan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

data class LaporanDto(
    val id: Long,
    val judul: String,
    val lokasi: String,
    val status: String,
    val petugas: String?
)

interface ApiLaporan {
    suspend fun semua(): List<LaporanDto>
}

class ApiLaporanSimulasi(var offline: Boolean = false) : ApiLaporan {
    override suspend fun semua(): List<LaporanDto> {
        if (offline) throw IllegalStateException("Tidak ada koneksi internet")
        return listOf(
            LaporanDto(1L, "Lampu Jalan Mati", "Gerbang Utama ITERA", "Diproses", "Budi"),
            LaporanDto(2L, "AC Ruang Kuliah Panas", "Gedung E Ruang 204", "Baru", null),
            LaporanDto(3L, "Kran Air Bocor", "Toilet Gedung C", "Selesai", "Sari")
        )
    }
}

class SumberLaporanOffline(
    private val database: AppDatabase,
    private val api: ApiLaporan
) {
    fun pantauSemua(): Flow<List<Laporan>> {
        return database.laporanQueries.semua()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun segarkan() {
        val terbaru = api.semua()
        database.transaction {
            database.laporanQueries.hapusSemua()
            terbaru.forEach { item ->
                database.laporanQueries.simpan(
                    id = item.id,
                    judul = item.judul,
                    lokasi = item.lokasi,
                    status = item.status,
                    petugas = item.petugas
                )
            }
        }
    }

    fun ambilDraf(): Draf? = database.drafQueries.ambil().executeAsOneOrNull()

    fun simpanDraf(judul: String, lokasi: String) {
        database.drafQueries.simpan(judul, lokasi)
    }

    fun hapusDraf() {
        database.drafQueries.hapus()
    }

    fun kirimLokal(judul: String, lokasi: String) {
        val nextId = (database.laporanQueries.semua().executeAsList().maxOfOrNull { it.id } ?: 0L) + 1L
        database.laporanQueries.simpan(
            id = nextId,
            judul = judul,
            lokasi = lokasi,
            status = "Baru",
            petugas = null
        )
        hapusDraf()
    }
}

@Composable
fun LaporKampusScreen(database: AppDatabase) {
    val api = remember { ApiLaporanSimulasi() }
    val sumber = remember { SumberLaporanOffline(database, api) }
    val daftarLaporan by sumber.pantauSemua().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    var judul by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var modeOffline by remember { mutableStateOf(false) }
    var pesanStatus by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sumber.ambilDraf()?.let { draf ->
            judul = draf.judul
            lokasi = draf.lokasi
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("LaporKampus: Offline-first & Draf Otomatis", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Simulasi Mode Offline")
            Switch(
                checked = modeOffline,
                onCheckedChange = {
                    modeOffline = it
                    api.offline = it
                }
            )
        }

        Button(
            onClick = {
                scope.launch {
                    pesanStatus = try {
                        sumber.segarkan()
                        "Berhasil menyegarkan data dari server"
                    } catch (e: Exception) {
                        "Gagal menyegarkan (${e.message}), menampilkan data lokal"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Segarkan Laporan")
        }

        if (pesanStatus.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(pesanStatus, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Form Laporan (Draf Tersimpan Otomatis)", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = judul,
            onValueChange = {
                judul = it
                sumber.simpanDraf(judul, lokasi)
            },
            label = { Text("Judul Laporan") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = lokasi,
            onValueChange = {
                lokasi = it
                sumber.simpanDraf(judul, lokasi)
            },
            label = { Text("Lokasi") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (judul.isNotBlank() && lokasi.isNotBlank()) {
                    sumber.kirimLokal(judul.trim(), lokasi.trim())
                    judul = ""
                    lokasi = ""
                    pesanStatus = "Laporan tersimpan dan draf dihapus"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Laporan")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Daftar Laporan (${daftarLaporan.size})", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        daftarLaporan.forEach { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "#${item.id} - ${item.judul}", fontWeight = FontWeight.Bold)
                    Text(text = "${item.lokasi} | Status: ${item.status}")
                    Text(text = "Petugas: ${item.petugas ?: "Belum ditugaskan"}", color = Color.Gray)
                }
            }
        }
    }
}