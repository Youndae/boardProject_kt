package com.example.boardproject_kt.domain.dto.board.request

import com.example.boardproject_kt.domain.entity.Board
import com.example.boardproject_kt.domain.entity.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BoardReplyRequest(
    @field:Size(min = 2, message = "제목은 2글자 이상이어야 합니다.")
    @field:NotBlank(message = "제목은 2글자 이상이어야 합니다.")
    val title: String,


    @field:Size(min = 2, message = "내용은 2글자 이상이어야 합니다.")
    @field:NotBlank(message = "내용은 2글자 이상이어야 합니다.")
    val content: String
) {
    fun toEntity(memberEntity: Member, targetBoard: Board): Board {
        return Board(
            title = this.title,
            content = this.content,
            member = memberEntity,
            groupNo = targetBoard.groupNo,
            indent = targetBoard.indent + 1
        )
    }
}
