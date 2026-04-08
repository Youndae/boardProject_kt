package com.example.boardproject_kt.domain.dto.imageBoard.response

import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardDetail
import java.time.LocalDate

data class ImageBoardDetailResponse(
    val title: String,
    val content: String,
    val writer: String,
    val writerId: String,
    val createdAt: LocalDate,
    val imageDataList: List<String>
) {
    companion object {
        fun of(detail: ImageBoardDetail, imageData: List<String>): ImageBoardDetailResponse {
            return ImageBoardDetailResponse(
                title = detail.title,
                content = detail.content,
                writer = detail.writer,
                writerId = detail.writerId,
                createdAt = detail.createdAt.toLocalDate(),
                imageDataList = imageData
            )
        }
    }
}
