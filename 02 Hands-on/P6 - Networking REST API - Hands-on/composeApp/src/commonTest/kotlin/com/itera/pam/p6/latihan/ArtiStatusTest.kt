package com.itera.pam.p6.latihan

import kotlin.test.Test
import kotlin.test.assertEquals

class ArtiStatusTest {
    @Test
    fun ujiCabang3xxDengan301() {
        assertEquals("Berhasil", artiStatus(200))
        assertEquals("Dialihkan ke alamat lain", artiStatus(301))
        assertEquals("Permintaan salah", artiStatus(404))
        assertEquals("Server bermasalah", artiStatus(500))
    }
}