package com.example.boardproject_kt.domain.dto.imageBoard.business

import java.time.LocalDateTime

data class ImageBoardDetail(
    val title: String,
    val content: String,
    val writer: String,
    val writerId: String,
    val createdAt: LocalDateTime
)
