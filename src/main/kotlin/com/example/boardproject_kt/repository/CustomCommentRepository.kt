package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.comment.response.BoardCommentResponse
import com.example.boardproject_kt.domain.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomCommentRepository {

    fun findAllCommentByBoardId(id: Long, pageable: Pageable): Page<BoardCommentResponse>

    fun findAllCommentByImageBoardId(id: Long, pageable: Pageable): Page<BoardCommentResponse>

    fun findNotDeleteCommentById(id: Long): Comment?
}