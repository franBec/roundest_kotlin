package dev.pollito.roundest_kotlin.util

import kotlin.random.Random

object RandomUtils {
    private const val POKEMON_ID_MIN = 1
    private const val POKEMON_ID_MAX = 151
    private const val MAX_COUNT = 10

    fun generateRandomIds(count: Int): List<Long> {
        require(count <= MAX_COUNT) { "Count cannot exceed $MAX_COUNT" }

        val ids = mutableSetOf<Long>()
        while (ids.size < count) {
            ids.add(POKEMON_ID_MIN + Random.nextLong((POKEMON_ID_MAX - POKEMON_ID_MIN).toLong()))
        }
        return ids.toList()
    }
}