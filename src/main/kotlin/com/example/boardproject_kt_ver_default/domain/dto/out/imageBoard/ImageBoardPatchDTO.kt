package com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard

import com.example.boardproject_kt_ver_default.domain.entity.ImageBoard

data class ImageBoardPatchDTO(
    val imageNo: Long,
    val imageTitle: String,
    val imageContent: String
){
    constructor(imageBoard: ImageBoard): this(
        imageBoard.imageNo,
        imageBoard.imageTitle,
        imageBoard.imageContent
    )
}
