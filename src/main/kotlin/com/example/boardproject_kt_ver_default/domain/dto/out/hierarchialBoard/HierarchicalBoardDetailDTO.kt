package com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard

import java.time.LocalDate

data class HierarchicalBoardDetailDTO(
    val boardNo: Long,
    val boardTitle: String,
    val boardContent: String,
    val nickname: String,
    val boardDate: LocalDate
)
