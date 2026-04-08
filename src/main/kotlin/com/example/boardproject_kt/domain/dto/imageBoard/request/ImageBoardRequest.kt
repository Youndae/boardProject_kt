package com.example.boardproject_kt.domain.dto.imageBoard.request

import com.example.boardproject_kt.domain.entity.ImageBoard
import com.example.boardproject_kt.domain.entity.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ImageBoardRequest (
    @field:Size(min = 2, message = "제목은 2글자 이상이어야 합니다.")
    @field:NotBlank(message = "제목은 2글자 이상이어야 합니다.")
    val title: String,

    @field:Size(min = 2, message = "내용은 2글자 이상이어야 합니다.")
    @field:NotBlank(message = "내용은 2글자 이상이어야 합니다.")
    val content: String
) {
    fun toEntity(member: Member): ImageBoard {
        return ImageBoard(
            member = member,
            title = this.title,
            content = this.content
        )
    }
}
