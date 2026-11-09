package com.itera.pam.p7.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.itera.pam.p7.db.AppDatabase
import com.itera.pam.p7.db.Catatan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class CatatanRepository(private val database: AppDatabase) {

    fun getAll(): Flow<List<Catatan>> {
        // TODO 1: kembalikan hasil query selectAll() sebagai Flow<List<Catatan>>
        return database.catatanQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun tambah(judul: String, isi: String) {
        // TODO 2: database.catatanQueries.insertCatatan(judul, isi)
        database.catatanQueries.insertCatatan(judul, isi)
    }

    fun hapus(id: Long) {
        // TODO 3: database.catatanQueries.deleteById(id)
        database.catatanQueries.deleteById(id)
    }
}

@Composable
fun Handson2Screen(database: AppDatabase) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hands-on 2: SQLDelight Repository")
        Text("CatatanRepository sudah dilengkapi! Silakan buka Latihan 3 untuk mencobanya langsung di UI.")
    }
}