package com.example.boardproject_kt_ver_default.domain.dto.out.comment

import java.time.LocalDate

data class CommentDTO(
    val commentNo: Long,
    val nickname: String,
    val commentDate: LocalDate,
    val commentContent: String,
    val commentGroupNo: Long,
    val commentIndent: Int,
    val commentUpperNo: String
)
