package com.itera.pam.p4.latihan

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Latihan 3: Todo App dengan ViewModel — implementasi MVVM pattern (slide P4 hal. 31)
//
// Checklist:
// [x] Data class Todo
// [x] TodoUiState dengan list
// [x] TodoViewModel dengan StateFlow
// [x] Add todo function
// [x] Toggle done function
// [x] TextField untuk input
// [x] LazyColumn untuk list
// [x] Checkbox untuk done

// 1. Data class
data class Todo(val id: Int, val text: String, val done: Boolean)

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val input: String = ""
)

// 2. ViewModel
class TodoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    fun onInputChange(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    fun addTodo() {
        val currentInput = _uiState.value.input.trim()
        if (currentInput.isBlank()) return

        _uiState.update { state ->
            val newTodo = Todo(
                id = state.todos.size,
                text = currentInput,
                done = false
            )
            state.copy(
                todos = state.todos + newTodo,
                input = ""
            )
        }
    }

    fun toggleTodo(id: Int) {
        _uiState.update { state ->
            state.copy(
                todos = state.todos.map { todo ->
                    if (todo.id == id) todo.copy(done = !todo.done) else todo
                }
            )
        }
    }
}

// 3. Compose UI
@Composable
fun Handson3Screen(viewModel: TodoViewModel = viewModel { TodoViewModel() }) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Latihan 3: Todo App dengan ViewModel")

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.input,
                onValueChange = viewModel::onInputChange,
                label = { Text("Tambah Todo baru") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(onClick = { viewModel.addTodo() }) {
                Text("Tambah")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
            items(uiState.todos, key = { it.id }) { todo ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.done,
                        onCheckedChange = { viewModel.toggleTodo(todo.id) }
                    )
                    Text(
                        text = todo.text,
                        textDecoration = if (todo.done) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
        }
    }
}