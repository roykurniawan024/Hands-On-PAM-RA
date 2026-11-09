package com.itera.pam.p7.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itera.pam.p7.db.AppDatabase

private const val KEY_JUMLAH_DIBUKA = "jumlah_dibuka"

@Composable
fun Handson1Screen(database: AppDatabase) {
    var jumlah by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        // TODO 1: baca nilai lama dari database (selectByKey langsung mengembalikan String?)
        val nilaiLama = database.preferensiQueries
            .selectByKey(KEY_JUMLAH_DIBUKA)
            .executeAsOneOrNull()
            ?.toLongOrNull() ?: 0L

        // TODO 2: hitung nilai baru = nilai lama + 1
        val nilaiBaru = nilaiLama + 1

        // TODO 3: simpan nilai baru dengan upsert
        database.preferensiQueries.upsert(KEY_JUMLAH_DIBUKA, nilaiBaru.toString())

        // TODO 4: update state `jumlah` dengan nilai baru tsb
        jumlah = nilaiBaru
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hands-on 1: Preferensi Sederhana")
        Text("Aplikasi ini sudah dibuka: $jumlah kali")
    }
}