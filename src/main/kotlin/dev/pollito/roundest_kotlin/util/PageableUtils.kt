package dev.pollito.roundest_kotlin.util

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object PageableUtils {

    fun createPageable(page: Int, size: Int, sort: List<String>): Pageable {
        var combinedSort: Sort = Sort.unsorted()
        var hasIdSort = false

        for (sortField in sort) {
            val sortParams = sortField.split(":")
            val direction = if (sortParams.size > 1 && sortParams[1].equals("desc", ignoreCase = true)) {
                Sort.Direction.DESC
            } else {
                Sort.Direction.ASC
            }

            if (sortParams[0].equals("id", ignoreCase = true)) {
                hasIdSort = true
            }

            combinedSort = combinedSort.and(Sort.by(direction, sortParams[0]))
        }

        if (!hasIdSort) {
            combinedSort = combinedSort.and(Sort.by(Sort.Direction.ASC, "id"))
        }

        return PageRequest.of(page, size, combinedSort)
    }
}