package com.itera.pam.p11.latihan

import com.itera.pam.p11.Counter
import com.itera.pam.p11.SessionLogger
import com.itera.pam.p11.di.appModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinModuleTest : KoinTest {

    // TODO 1: deklarasikan 2 property berikut:
    private val counter: Counter by inject()
    private val sessionLogger: SessionLogger by inject()

    @BeforeTest
    fun setup() {
        // TODO 2: startKoin { modules(appModule) }
        startKoin {
            modules(appModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun semuaDependencyDiAppModuleBerhasilDiResolve() {
        // TODO 3: assertNotNull(counter) dan assertNotNull(sessionLogger)
        assertNotNull(counter)
        assertNotNull(sessionLogger)
    }
}