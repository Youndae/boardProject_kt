package com.example.boardproject_kt_ver_default.useCase.comment

import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentInsertDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentReplyInsertDTO
import com.example.boardproject_kt_ver_default.service.comment.CommentWriteService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class CommentWriteUseCase(
    private val commentWriteService: CommentWriteService
) {
    fun postBoardComment(boardNo: Long, commentDTO: CommentInsertDTO, principal: Principal): String {
        return commentWriteService.postBoardComment(boardNo, commentDTO, principal)
    }

    fun postImageBoardComment(boardNo: Long, commentDTO: CommentInsertDTO, principal: Principal): String {
        return commentWriteService.postImageBoardComment(boardNo, commentDTO, principal)
    }

    fun postBoardCommentReply(boardNo: Long, commentDTO: CommentReplyInsertDTO, principal: Principal): String {
        return commentWriteService.postBoardCommentReply(boardNo, commentDTO, principal)
    }

    fun postImageBoardCommentReply(boardNo: Long, commentDTO: CommentReplyInsertDTO, principal: Principal): String {
        return commentWriteService.postImageBoardCommentReply(boardNo, commentDTO, principal)
    }

    fun deleteComment(commentNo: Long, principal: Principal): String {
        return commentWriteService.deleteComment(commentNo, principal)
    }
}