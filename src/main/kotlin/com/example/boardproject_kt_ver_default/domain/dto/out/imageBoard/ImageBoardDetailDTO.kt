package com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard

import com.example.boardproject_kt_ver_default.domain.entity.ImageBoard
import java.time.LocalDate

data class ImageBoardDetailDTO(
    val imageNo: Long,
    val imageTitle: String,
    val nickname: String,
    val imageDate: LocalDate,
    val imageContent: String,
    val imageData: List<ImageDataDTO>
) {
    constructor(imageBoard: ImageBoard, imageDataDTO: List<ImageDataDTO>): this(
        imageBoard.imageNo,
        imageBoard.imageTitle,
        imageBoard.member.nickName,
        imageBoard.imageDate!!,
        imageBoard.imageContent,
        imageDataDTO
    )
}
