package com.itera.pam.p6.latihan

fun artiStatus(kode: Int): String = when (kode) {
    in 200..299 -> "Berhasil"
    in 300..399 -> "Dialihkan ke alamat lain"
    in 400..499 -> "Permintaan salah"
    in 500..599 -> "Server bermasalah"
    else -> "Status lain: $kode"
}