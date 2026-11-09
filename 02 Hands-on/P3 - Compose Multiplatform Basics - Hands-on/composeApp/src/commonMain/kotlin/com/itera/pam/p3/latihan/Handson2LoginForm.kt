package com.itera.pam.p3.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Hands-on 2: Login Form (lihat slide "P3 - Compose Multiplatform Basics.pdf" hal. 31)
// Tugas: Buat form login dengan username, password, dan tombol login.

@Composable
fun LoginForm(onLogin: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO 1: Text judul "Login"
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 2: OutlinedTextField untuk username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO 3: OutlinedTextField untuk password dengan PasswordVisualTransformation()
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 4: Button "Login" dengan onClick = { onLogin(username, password) }
        Button(
            onClick = { onLogin(username, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}

@Composable
fun Handson2Screen() {
    var message by remember { mutableStateOf("") }

    Column {
        LoginForm(onLogin = { user, _ ->
            message = "Login berhasil sebagai: $user"
        })
        if (message.isNotEmpty()) {
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}