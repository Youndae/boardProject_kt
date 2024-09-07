package com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard

import java.time.LocalDate

data class HierarchicalBoardListDTO(
    val boardNo: Long,
    val boardTitle: String,
    val nickname: String,
    val boardDate: LocalDate,
    val boardIndent: Int
)
