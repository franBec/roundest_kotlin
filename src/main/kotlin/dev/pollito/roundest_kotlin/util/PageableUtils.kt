package dev.pollito.roundest_kotlin.util

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

private const val DEFAULT_SORT_PROPERTY = "id"

object PageableUtils {

  fun createPageable(page: Int, size: Int, sort: List<String>): Pageable {
    val processedSort =
        if (sort.size == 1 && sort[0] == "[$DEFAULT_SORT_PROPERTY:asc]") {
          listOf(DEFAULT_SORT_PROPERTY)
        } else {
          sort
        }

    var combinedSort: Sort = Sort.unsorted()
    var hasDefaultSortProperty = false

    for (sortField in processedSort) {
      val sortParams = sortField.split(":")
      val direction =
          if (sortParams.size > 1 && sortParams[1].equals("desc", ignoreCase = true)) {
            Sort.Direction.DESC
          } else {
            Sort.Direction.ASC
          }

      if (sortParams[0].equals(DEFAULT_SORT_PROPERTY, ignoreCase = true)) {
        hasDefaultSortProperty = true
      }

      combinedSort = combinedSort.and(Sort.by(direction, sortParams[0]))
    }

    if (!hasDefaultSortProperty) {
      combinedSort = combinedSort.and(Sort.by(Sort.Direction.ASC, DEFAULT_SORT_PROPERTY))
    }

    return PageRequest.of(page, size, combinedSort)
  }
}
