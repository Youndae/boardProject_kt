package com.example.boardproject_kt.usecase.comment

import com.example.boardproject_kt.domain.dto.comment.response.BoardCommentResponse
import com.example.boardproject_kt.domain.dto.response.PageResponse
import com.example.boardproject_kt.domain.enumuration.ListAmount
import com.example.boardproject_kt.service.comment.CommentDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {  }

@Service
class CommentReadUseCase(
    private val commentDataService: CommentDataService
) {

    fun getBoardCommentList(
        id: Long,
        page: Int
    ): PageResponse<BoardCommentResponse> {
        val pageable = createCommentPageable(page)

        val content = commentDataService.findAllCommentByBoardId(id, pageable)

        return PageResponse.of(content)
    }

    fun getImageBoardCommentList(
        id: Long,
        page: Int
    ): PageResponse<BoardCommentResponse> {
        val pageable = createCommentPageable(page)

        val content = commentDataService.findAllCommentByImageBoardId(id, pageable)

        return PageResponse.of(content)
    }

    private fun createCommentPageable(page: Int): Pageable {
        return PageRequest.of(
            page - 1,
            ListAmount.COMMENT.key
        )
    }

}