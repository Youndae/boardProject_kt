package com.example.boardproject_kt_ver_default.repository.comment

import com.example.boardproject_kt_ver_default.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long>, CommentCustomRepository {
}