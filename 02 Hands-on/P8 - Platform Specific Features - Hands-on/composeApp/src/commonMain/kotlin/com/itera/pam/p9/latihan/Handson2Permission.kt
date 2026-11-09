package com.itera.pam.p9.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itera.pam.p9.hasCameraPermission

// Hands-on 2: Permission Check (Platform-Aware)
// `hasCameraPermission()` sudah diimplementasikan lewat expect/actual: cek
// ASLI di Android (ContextCompat.checkSelfPermission), stub `true` di
// iOS/Desktop karena model permission-nya berbeda dan di luar cakupan
// hands-on ini.

@Composable
fun Handson2Screen() {
    // TODO 2: Ambil status izin kamera memakai hasCameraPermission()
    var isGranted by remember { mutableStateOf(hasCameraPermission()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hands-on 2: Permission Check")

        Spacer(modifier = Modifier.height(8.dp))

        // TODO 2 & 3: Tampilkan status izin kamera dan pesan berbeda tergantung true/false-nya
        Text("Status izin kamera: ${if (isGranted) "Diizinkan (true)" else "Belum Diizinkan (false)"}")

        if (isGranted) {
            Text("✅ Kamera siap digunakan!")
        } else {
            Text("⚠️ Akses kamera belum diberikan. Silakan berikan izin kamera terlebih dahulu.")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            // Cek ulang / simulasi perubahan izin
            isGranted = hasCameraPermission()
        }) {
            Text("Simulasikan Minta Izin")
        }
    }
}