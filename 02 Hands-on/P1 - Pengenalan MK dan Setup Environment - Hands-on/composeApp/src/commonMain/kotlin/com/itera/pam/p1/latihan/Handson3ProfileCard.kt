package com.itera.pam.p1.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itera.pam.p1.getPlatformName

// Hands-on 3: Layout Dasar — Profile Card
// Tugas: Susun sebuah "kartu profil" sederhana berisi nama, NIM, dan platform
// yang sedang berjalan, menggunakan Card, Column, Row, dan Modifier.
//
// CATATAN: fungsi ini belum menampilkan apa-apa selain placeholder di bawah —
// lengkapi semua TODO supaya kartu profil muncul dengan benar.

@Composable
fun Handson3Screen() {
    // TODO 1: Bungkus semua konten dengan Card
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // TODO 2: Di dalam Card, buat Column dengan padding 16.dp
        Column(modifier = Modifier.padding(16.dp)) {
            // TODO 3: Tambahkan Text() untuk nama kamu
            Text("Nama: Roy Kurniawan")

            // TODO 4: Tambahkan Text() untuk NIM kamu
            Text("NIM: 124140024")

            // TODO 5: Tambahkan Row berisi label Platform dan getPlatformName()
            Row {
                Text("Platform: ")
                Text(getPlatformName())
            }
        }
    }
}