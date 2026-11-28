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
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

fun pesanUntuk(e: Throwable): String = when (e) {
    is CancellationException -> throw e
    is RedirectResponseException -> "Permintaan dialihkan (${e.response.status.value})."
    is ClientRequestException -> when (e.response.status.value) {
        401, 403 -> "Akses ditolak."
        404 -> "Data tidak ditemukan."
        else -> "Permintaan tidak valid (${e.response.status.value})."
    }
    is ServerResponseException -> "Server sedang bermasalah (${e.response.status.value})."
    is HttpRequestTimeoutException,
    is ConnectTimeoutException,
    is SocketTimeoutException -> "Koneksi timeout. Coba lagi nanti."
    is JsonConvertException,
    is SerializationException -> "Format data dari server tidak sesuai."
    is IOException -> "Tidak ada koneksi internet atau server tidak terjangkau."
    else -> "Terjadi kesalahan: ${e.message ?: "tidak diketahui"}"
}

class UserRepository {
    suspend fun getUsers(): List<RemoteUser> = fetchUsers()
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val users: List<RemoteUser>) : UiState
    data class Error(val message: String) : UiState
}

class UserListStateHolder(
    private val repository: UserRepository = UserRepository()
) {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    suspend fun load() {
        _state.value = UiState.Loading
        try {
            val users = repository.getUsers()
            _state.value = UiState.Success(users)
        } catch (e: Exception) {
            _state.value = UiState.Error(pesanUntuk(e))
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
            UiState.Loading -> Text("Sedang memuat data (Loading)...")
            is UiState.Success -> {
                Text("Berhasil memuat ${state.users.size} user:")
                Spacer(modifier = Modifier.height(4.dp))
                state.users.forEach { user ->
                    Text("- ${user.name} (${user.email})")
                }
            }
            is UiState.Error -> Text("Error: ${state.message}")
        }
    }
}