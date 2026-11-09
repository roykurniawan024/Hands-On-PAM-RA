package com.itera.pam.p6.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Hands-on 2: Repository Pattern & Loading State
// Bungkus fetchUsers() (dari Handson1KtorClient.kt) di balik sebuah Repository,
// lalu buat state holder sederhana yang merepresentasikan status pengambilan
// data: Loading / Success / Error.

class UserRepository {
    suspend fun getUsers(): List<RemoteUser> = fetchUsers()
}

sealed class UiState {
    data object Loading : UiState()
    data class Success(val users: List<RemoteUser>) : UiState()
    data class Error(val message: String) : UiState()
}

class UserListStateHolder(
    private val repository: UserRepository = UserRepository()
) {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    suspend fun load() {
        // TODO 1: set _state.value ke UiState.Loading sebelum mulai fetch
        _state.value = UiState.Loading
        try {
            // TODO 2: panggil repository.getUsers() di dalam try-catch
            val users = repository.getUsers()
            // TODO 3: jika berhasil, set _state.value ke UiState.Success(users)
            _state.value = UiState.Success(users)
        } catch (e: Exception) {
            // TODO 4: jika gagal (catch Exception e), set _state.value ke UiState.Error(...)
            _state.value = UiState.Error(e.message ?: "Terjadi kesalahan")
        }
    }
}

@Composable
fun Handson2Screen() {
    val stateHolder = remember { UserListStateHolder() }
    val uiState by stateHolder.state.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hands-on 2: Repository Pattern & Loading State")
        Spacer(modifier = Modifier.height(8.dp))

        when (val state = uiState) {
            is UiState.Loading -> Text("Sedang memuat data (Loading)...")
            is UiState.Success -> {
                Text("Berhasil memuat ${state.users.size} user:")
                Spacer(modifier = Modifier.height(4.dp))
                state.users.forEach { user ->
                    Text("• ${user.name} (${user.email})")
                }
            }
            is UiState.Error -> Text("Error: ${state.message}")
        }
    }
}