package com.itera.pam.p11.latihan

import com.itera.pam.p11.ClockProvider
import com.itera.pam.p11.SessionLogger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource
import kotlin.time.TimeSource

// Hands-on 2: Test Double — Fake ClockProvider
// Solusi: gunakan TestTimeSource dari kotlin.time — "jam palsu" yang hanya
// maju kalau kita suruh maju secara manual, sehingga hasilnya deterministik.

// TODO 1: delegasikan seluruh kontrak TimeSource ke `source`
class FakeClockProvider(
    val source: TestTimeSource = TestTimeSource()
) : ClockProvider, TimeSource by source

class SessionLoggerTest {

    @Test
    fun endSessionMengembalikanDurasiYangDimajukanPadaFakeClock() {
        // TODO 2: buat instance FakeClockProvider()
        val fakeClock = FakeClockProvider()

        // TODO 3: buat SessionLogger(fakeClock), lalu panggil startSession()
        val logger = SessionLogger(fakeClock)
        logger.startSession()

        // TODO 4: majukan waktu palsu 5 detik
        fakeClock.source += 5.seconds

        // TODO 5: assertEquals(5.seconds, logger.endSession())
        assertEquals(5.seconds, logger.endSession())
    }
}