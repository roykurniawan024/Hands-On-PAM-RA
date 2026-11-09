package com.itera.pam.p10.latihan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itera.pam.p10.MockAiRepository
import kotlinx.coroutines.launch

// Hands-on 3: AI-Powered Chat UI
// Tugas: buat UI chat sederhana: TextField input, tombol "Kirim", daftar
// bubble pesan (isUser: Boolean, text: String) yang bisa di-scroll, dan
// indikator loading saat menunggu jawaban dari AiRepository.ask()
// (pakai MockAiRepository() secara default).

data class ChatMessage(val isUser: Boolean, val text: String)

@Composable
fun Handson3Screen() {
    // TODO 1: Buat state `messages`
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }

    // TODO 2: Buat state `input` dan `isLoading`
    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val repository = remember { MockAiRepository() }
    val scope = rememberCoroutineScope()

    // TODO 3: Buat Column berisi LazyColumn, CircularProgressIndicator, dan Row input + tombol Kirim
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Hands-on 3: AI-Powered Chat UI")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp, max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (message.isUser) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            }
                        )
                    ) {
                        Text(
                            text = if (message.isUser) "Anda: ${message.text}" else "AI: ${message.text}",
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Tulis pesan...") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            // TODO 4: Saat "Kirim" diklik
            Button(
                enabled = !isLoading && input.isNotBlank(),
                onClick = {
                    val pertanyaan = input.trim()
                    if (pertanyaan.isNotEmpty()) {
                        messages = messages + ChatMessage(isUser = true, text = pertanyaan)
                        input = ""
                        isLoading = true

                        scope.launch {
                            val reply = repository.ask(pertanyaan).getOrElse {
                                "Error: ${it.message}"
                            }
                            messages = messages + ChatMessage(isUser = false, text = reply)
                            isLoading = false
                        }
                    }
                }
            ) {
                Text("Kirim")
            }
        }
    }
}