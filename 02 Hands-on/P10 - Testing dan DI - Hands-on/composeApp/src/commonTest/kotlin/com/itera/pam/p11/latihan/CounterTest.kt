package com.itera.pam.p11.latihan

import com.itera.pam.p11.Counter
import kotlin.test.Test
import kotlin.test.assertEquals

class CounterTest {

    @Test
    fun incrementMenambahNilaiCounter() {
        val counter = Counter()
        counter.increment()
        counter.increment()
        assertEquals(2, counter.value.value)
    }

    @Test
    fun decrementTidakBolehMembuatNilaiDiBawahNol() {
        val counter = Counter()
        counter.decrement()
        assertEquals(0, counter.value.value)
    }

    @Test
    fun resetMengembalikanNilaiKeNol() {
        val counter = Counter()
        counter.increment()
        counter.increment()
        counter.increment()
        counter.reset()
        assertEquals(0, counter.value.value)
    }
}