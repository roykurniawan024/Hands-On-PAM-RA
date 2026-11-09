package com.itera.pam.p4.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Latihan 2: Form dengan State Hoisting (slide P4 hal. 30)
// Tugas: Buat reusable TextField component dengan state hoisting —
// komponennya sendiri STATELESS (tidak punya state internal), state-nya
// dipegang oleh parent (RegistrationForm) dan dikirim turun lewat parameter.
//
// Checklist:
// [x] Stateless LabeledTextField
// [x] Parameter: value, onValueChange
// [x] State hoisted ke parent
// [x] Name dan Email field
// [x] Preview yang menampilkan "Hello, [name]!"
// [x] Gunakan 1 komponen untuk kedua field

// Stateless TextField component
@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    // Implement dengan OutlinedTextField
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true
    )
}

// Parent yang menyimpan state
@Composable
fun Handson2Screen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Latihan 2: Form dengan State Hoisting")

        Spacer(modifier = Modifier.height(8.dp))

        LabeledTextField(
            label = "Name",
            value = name,
            onValueChange = { name = it }
        )

        // Tambahkan email field pakai LabeledTextField yang sama, hoisted ke `email`
        LabeledTextField(
            label = "Email",
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tampilkan preview data
        Text("Hello, $name! Email: $email")
    }
}