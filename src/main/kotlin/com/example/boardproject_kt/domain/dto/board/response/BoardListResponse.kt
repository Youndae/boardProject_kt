package com.example.boardproject_kt.domain.dto.board.response

import java.time.LocalDate
import java.time.LocalDateTime

data class BoardListResponse(
    val id: Long,
    val title: String,
    val writer: String,
    val createdAt: LocalDate,
    val indent: Int
) {
    constructor(
        id: Long,
        title: String,
        writer: String,
        createdAt: LocalDateTime,
        indent: Int
    ) : this(
        id = id,
        title = title,
        writer = writer,
        createdAt = createdAt.toLocalDate(),
        indent = indent
    )
}
