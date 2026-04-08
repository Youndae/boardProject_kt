package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>, CustomCommentRepository {
}