package com.itera.pam.p7.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbDir = File(System.getProperty("user.home"), ".handson-pam-p7")
        if (!dbDir.exists()) dbDir.mkdirs()
        val dbFile = File(dbDir, "catatan_p7.db")
        val isNewDatabase = !dbFile.exists() || dbFile.length() == 0L
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        if (isNewDatabase) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}