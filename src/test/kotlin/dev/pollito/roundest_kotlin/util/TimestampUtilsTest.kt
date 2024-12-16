package dev.pollito.roundest_kotlin.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TimestampUtilsTest {

    @Test
    fun `now returns the current ISO timestamp`() {
        val timestampProvider = object : TimestampUtils {}
        val iso8061DateFormat = """\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z""".toRegex()
        assertTrue(iso8061DateFormat.matches(timestampProvider.now()))
    }
}