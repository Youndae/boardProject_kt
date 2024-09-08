package com.example.boardproject_kt_ver_default.service.comment

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.comment.CommentDTO
import com.example.boardproject_kt_ver_default.repository.comment.CommentRepository
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CommentReadService(
    private val commentRepository: CommentRepository
) {

    private val log = logger()

    fun getBoardCommentList(boardNo: Long, paginationDTO: PaginationDTO): Page<CommentDTO> {
        val pageable = getCommentPageable(paginationDTO)

        val result = commentRepository.getList(boardNo, null, pageable)

        log.info("CommentService::findAll : {}", result)

        return result
    }

    fun getImageCommentList(imageNo: Long, paginationDTO: PaginationDTO): Page<CommentDTO> {
        val pageable = getCommentPageable(paginationDTO)
        return commentRepository.getList(null, imageNo, pageable)
    }

    fun getCommentPageable(paginationDTO: PaginationDTO): Pageable {
        return PageRequest.of(paginationDTO.pageNum - 1
                                , paginationDTO.boardAmount
                                , Sort.by("commentGroupNo").descending()
                                        .and(Sort.by("commentUpperNo").ascending())
                            )
    }
}