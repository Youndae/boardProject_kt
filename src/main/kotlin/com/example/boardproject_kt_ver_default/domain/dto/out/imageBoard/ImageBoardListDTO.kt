package com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard

import java.time.LocalDate

data class ImageBoardListDTO(
    val imageNo: Long,
    val imageTitle: String,
    val nickname: String,
    val imageDate: LocalDate,
    val imageName: String
)
