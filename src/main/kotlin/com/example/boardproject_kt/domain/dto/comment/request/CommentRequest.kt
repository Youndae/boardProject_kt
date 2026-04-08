package com.example.boardproject_kt.domain.dto.comment.request

import com.example.boardproject_kt.domain.entity.Board
import com.example.boardproject_kt.domain.entity.Comment
import com.example.boardproject_kt.domain.entity.ImageBoard
import com.example.boardproject_kt.domain.entity.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CommentRequest(
    @field:Size(min = 2, message = "게시글 내용은 2글자 이상이어야 합니다.")
    @field:NotBlank(message = "게시글 내용은 2글자 이상이어야 합니다.")
    val content: String
) {
    fun toEntity(
        memberEntity: Member,
        boardEntity: Board
    ) : Comment {
        return Comment(
            member = memberEntity,
            content = this.content,
            indent = 0,
            imageBoard = null,
            board = boardEntity
        )
    }

    fun toEntity(
        memberEntity: Member,
        imageBoardEntity: ImageBoard
    ) : Comment {
        return Comment(
            member = memberEntity,
            content = this.content,
            indent = 0,
            imageBoard = imageBoardEntity,
            board = null
        )
    }
}
