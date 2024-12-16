package dev.pollito.roundest_kotlin.util

import java.time.Instant
import java.time.format.DateTimeFormatter

interface TimestampUtils {
    fun now(): String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
}