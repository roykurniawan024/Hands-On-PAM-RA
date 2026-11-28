package com.itera.pam.p7

import com.itera.pam.p7.db.AppDatabase
import com.itera.pam.p7.db.DatabaseDriverFactory
import com.itera.pam.p7.latihan.ApiLaporanSimulasi
import com.itera.pam.p7.latihan.SumberLaporanOffline
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() = runBlocking {
    val driver1 = DatabaseDriverFactory().createDriver()
    val db1 = AppDatabase(driver1)

    println("=== 1. Uji simpan() 2x lalu executeAsList() ===")
    db1.laporanQueries.hapusSemua()
    db1.laporanQueries.simpan(1L, "Lampu Jalan Mati", "Gerbang Utama ITERA", "Diproses", "Budi")
    db1.laporanQueries.simpan(2L, "AC Ruang Kuliah Panas", "Gedung E Ruang 204", "Baru", null)
    val hasilSimpan = db1.laporanQueries.semua().executeAsList()
    hasilSimpan.forEach { println(it) }

    println("\n=== 2. Eksperimen urutan segarkan() saat offline ===")
    val api = ApiLaporanSimulasi(offline = false)
    val sumber = SumberLaporanOffline(db1, api)
    sumber.segarkan()
    println("Jumlah data setelah segarkan online: ${db1.laporanQueries.semua().executeAsList().size}")

    api.offline = true
    try {
        sumber.segarkan()
    } catch (e: Exception) {
        println("Urutan aman gagal (${e.message}) -> data tersisa: ${db1.laporanQueries.semua().executeAsList().size}")
    }

    try {
        db1.laporanQueries.hapusSemua()
        api.semua()
    } catch (e: Exception) {
        println("Urutan terbalik gagal (${e.message}) -> data tersisa: ${db1.laporanQueries.semua().executeAsList().size}")
    }

    api.offline = false
    sumber.segarkan()
    println("Dipulihkan ke urutan aman -> data kembali: ${db1.laporanQueries.semua().executeAsList().size}")

    println("\n=== 3. Uji persistensi berkas desktop lintas sesi ===")
    db1.catatanQueries.insertCatatan("Uji Desktop", "Data harus bertahan setelah restart")
    db1.drafQueries.simpan("Draf Kursi Rusak", "Gedung C Ruang 102")
    driver1.close()

    val driver2 = DatabaseDriverFactory().createDriver()
    val db2 = AppDatabase(driver2)
    val catatanTersimpan = db2.catatanQueries.selectAll().executeAsList()
    val laporanTersimpan = db2.laporanQueries.semua().executeAsList()
    val drafTersimpan = db2.drafQueries.ambil().executeAsOneOrNull()
    val dbFile = File(System.getProperty("user.home"), ".handson-pam-p7/catatan_p7.db")

    println("File DB: ${dbFile.absolutePath} (${dbFile.length()} bytes)")
    println("Catatan setelah restart: ${catatanTersimpan.size} item -> ${catatanTersimpan.firstOrNull()}")
    println("Laporan setelah restart: ${laporanTersimpan.size} item")
    println("Draf setelah restart: $drafTersimpan")
    driver2.close()
}