package com.itera.pam.p6.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Hands-on 3: Tampilkan Data di Compose dengan Loading/Error UI
// Tugas: pakai UserListStateHolder (dari Handson2Repository.kt) untuk
// menampilkan:
// - CircularProgressIndicator() selagi UiState.Loading
// - LazyColumn berisi nama & email user selagi UiState.Success
// - Pesan error + tombol "Coba Lagi" (yang memanggil ulang load()) selagi
//   UiState.Error

@Composable
fun Handson3Screen() {
    // TODO 1: buat state holder
    val holder = remember { UserListStateHolder() }
    // TODO 2: collect state-nya
    val state by holder.state.collectAsState()
    // TODO 4: buat scope coroutine untuk tombol retry
    val scope = rememberCoroutineScope()

    // TODO 3: panggil holder.load() sekali saat pertama kali screen ini muncul
    LaunchedEffect(Unit) {
        holder.load()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hands-on 3: User List (Loading / Success / Error UI)")
        Spacer(modifier = Modifier.height(12.dp))

        // TODO 5: buat `when (val currentState = state)` yang menangani 3 kasus
        when (val currentState = state) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp)
                ) {
                    items(currentState.users, key = { it.id }) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = user.name, fontWeight = FontWeight.Bold)
                                Text(text = user.email, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(text = "Gagal memuat data: ${currentState.message}", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    scope.launch {
                        holder.load()
                    }
                }) {
                    Text("Coba Lagi")
                }
            }
        }
    }
}