package com.example.boardproject_kt.domain.dto.comment.response

import java.time.LocalDate
import java.time.LocalDateTime

data class BoardCommentResponse(
    val id: Long,
    val writer: String,
    val writerId: String,
    val createdAt: LocalDate,
    val content: String,
    val indent: Int,
    val isDeleted: Boolean
) {
    constructor(
        id: Long,
        nickname: String,
        userId: String,
        createdAt: LocalDateTime,
        content: String,
        indent: Int,
        deletedAt: LocalDateTime?
    ): this(
        id = id,
        writer = if (deletedAt == null) nickname else "",
        writerId = if (deletedAt == null) userId else "",
        createdAt = createdAt.toLocalDate(),
        content = if(deletedAt == null) content else "삭제된 댓글입니다.",
        indent = indent,
        isDeleted = deletedAt != null
    )


}
