package com.example.boardproject_kt.domain.dto.board.response

import java.time.LocalDate
import java.time.LocalDateTime

data class BoardDetailResponse(
    val title: String,
    val writer: String,
    val writerId: String,
    val content: String,
    val createdAt: LocalDate
) {
    constructor(
        title: String,
        writer: String,
        writerId: String,
        content: String,
        createdAt: LocalDateTime
    ) : this(
        title = title,
        writer = writer,
        writerId = writerId,
        content = content,
        createdAt = createdAt.toLocalDate()
    )
}
