package com.example.boardproject_kt_ver_default.useCase.comment

import com.example.boardproject_kt_ver_default.domain.dto.out.comment.CommentDTO
import com.example.boardproject_kt_ver_default.domain.mapper.PaginationRequestMapper
import com.example.boardproject_kt_ver_default.service.comment.CommentReadService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class CommentReadUseCase(
    private val commentReadService: CommentReadService,
    private val paginationRequestMapper: PaginationRequestMapper
) {
    fun getBoardCommentList(boardNo: Long, pageNum: Int): Page<CommentDTO> {
        val paginationDTO = paginationRequestMapper.toPaginationDTO(pageNum)

        return commentReadService.getBoardCommentList(boardNo, paginationDTO)
    }

    fun getImageCommentList(imageNo: Long, pageNum: Int): Page<CommentDTO> {
        val paginationDTO = paginationRequestMapper.toPaginationDTO(pageNum)

        return commentReadService.getImageCommentList(imageNo, paginationDTO)
    }


}