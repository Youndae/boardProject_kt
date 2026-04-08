package com.example.boardproject_kt.domain.dto.response

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val items: List<T>,
    val totalPages: Int,
    val isEmpty: Boolean,
    val currentPage: Int
) {
    companion object {
        fun <T> of(content: Page<T>): PageResponse<T> =
            PageResponse(
                items = content.content,
                totalPages = content.totalPages,
                isEmpty = content.isEmpty,
                currentPage = content.number
            )
    }
}
