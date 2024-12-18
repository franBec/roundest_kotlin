package dev.pollito.roundest_kotlin.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class PageableUtilsTest {

  @Test
  fun `should create Pageable with default Sort when no Sort fields provided`() {
    val page = 0
    val size = 10
    val sort = emptyList<String>()

    val pageable: Pageable = PageableUtils.createPageable(page, size, sort)

    assertEquals(page, pageable.pageNumber)
    assertEquals(size, pageable.pageSize)
    assertEquals(Sort.by(Sort.Direction.ASC, "id"), pageable.sort)
  }

  @Test
  fun `should default to Asc when no Direction is provided`() {
    val pageable: Pageable = PageableUtils.createPageable(0, 10, listOf("name"))
    assertTrue(pageable.sort.getOrderFor("name")?.isAscending == true)
  }

  @Test
  fun `should add id Sort if not provided`() {
    val pageable: Pageable = PageableUtils.createPageable(0, 10, listOf("votes:desc"))

    assertTrue(pageable.sort.getOrderFor("votes")?.isDescending == true)
    assertTrue(pageable.sort.getOrderFor("id")?.isAscending == true)
  }

  @Test
  fun `should not add id Sort if already provided`() {
    val pageable: Pageable = PageableUtils.createPageable(0, 10, listOf("id:desc"))

    assertEquals(Sort.by(Sort.Direction.DESC, "id"), pageable.sort)
  }
}
