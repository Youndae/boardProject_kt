package com.example.boardproject_kt.service.comment

import com.example.boardproject_kt.domain.dto.comment.response.BoardCommentResponse
import com.example.boardproject_kt.domain.entity.Comment
import com.example.boardproject_kt.repository.CommentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommentDataService(
    private val commentRepository: CommentRepository
) {

    fun findAllCommentByBoardId(id: Long, pageable: Pageable): Page<BoardCommentResponse> =
        commentRepository.findAllCommentByBoardId(id, pageable)

    fun findAllCommentByImageBoardId(id: Long, pageable: Pageable): Page<BoardCommentResponse> =
        commentRepository.findAllCommentByImageBoardId(id, pageable)

    fun saveComment(comment: Comment): Comment =
        commentRepository.save(comment)

    fun findNotDeleteCommentById(id: Long): Comment? =
        commentRepository.findNotDeleteCommentById(id)

    fun deleteComment(comment: Comment) =
        commentRepository.delete(comment)
}