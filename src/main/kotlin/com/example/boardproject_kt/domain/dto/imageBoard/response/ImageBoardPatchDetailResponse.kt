package com.example.boardproject_kt.domain.dto.imageBoard.response

data class ImageBoardPatchDetailResponse(
    val title: String,
    val content: String,
    val imageList: List<ImageDataResponse>
)
