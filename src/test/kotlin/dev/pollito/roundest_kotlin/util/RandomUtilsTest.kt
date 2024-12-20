package dev.pollito.roundest_kotlin.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RandomUtilsTest {
  @Test
  fun `should generate random ids with valid count`() {
    val count = 2

    val randomIds: List<Long> = RandomUtils.generateRandomIds(count)

    assertEquals(count, randomIds.size)
    assertEquals(randomIds.size, randomIds.distinct().count())
    assertTrue(randomIds.all { it in 1..151 })
  }

  @Test
  fun `should throw IllegalArgumentException when count is too much`() {
    assertThrows<IllegalArgumentException> { RandomUtils.generateRandomIds(99) }
  }

  @Test
  fun `should generate empty List when count is less than 1`() {
    assertTrue(RandomUtils.generateRandomIds(0).isEmpty())
    assertTrue(RandomUtils.generateRandomIds(-1).isEmpty())
  }
}
