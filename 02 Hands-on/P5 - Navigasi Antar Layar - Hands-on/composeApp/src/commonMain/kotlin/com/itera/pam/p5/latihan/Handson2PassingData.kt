package com.itera.pam.p5.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
internal data object Handson2Home

@Serializable
internal data class Handson2Detail(val itemId: Int)

private data class Item(val id: Int, val nama: String)

private val daftarItem = listOf(
    Item(1, "Buku Kotlin"),
    Item(2, "Laptop"),
    Item(3, "Headphone"),
)

@Composable
fun Handson2Screen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Handson2Home) {
        composable<Handson2Home> {
            HomeListScreen(navController)
        }
        composable<Handson2Detail> { backStackEntry ->
            val route = backStackEntry.toRoute<Handson2Detail>()
            DetailItemScreen(navController, route.itemId)
        }
    }
}

@Composable
private fun HomeListScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Daftar Item")
        daftarItem.forEach { item ->
            Button(onClick = {
                navController.navigate(Handson2Detail(item.id))
            }) {
                Text(item.nama)
            }
        }
    }
}

@Composable
private fun DetailItemScreen(navController: NavHostController, itemId: Int) {
    val item = daftarItem.find { it.id == itemId }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detail Item")
        Text("ID: $itemId")
        Text("Nama: ${item?.nama ?: "(tidak ditemukan)"}")
        Button(onClick = { navController.popBackStack() }) {
            Text("< Kembali")
        }
    }
}