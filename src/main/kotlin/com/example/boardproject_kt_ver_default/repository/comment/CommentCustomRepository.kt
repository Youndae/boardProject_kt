package com.example.boardproject_kt_ver_default.repository.comment

import com.example.boardproject_kt_ver_default.domain.dto.out.comment.CommentDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentCustomRepository {

    fun getList(boardNo: Long?, imageNo: Long?, pageable: Pageable): Page<CommentDTO>


}