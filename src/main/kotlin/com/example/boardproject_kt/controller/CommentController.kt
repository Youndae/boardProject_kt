package com.example.boardproject_kt.controller

import com.example.boardproject_kt.domain.dto.comment.request.CommentRequest
import com.example.boardproject_kt.domain.dto.comment.response.BoardCommentResponse
import com.example.boardproject_kt.domain.dto.response.ApiResponse
import com.example.boardproject_kt.domain.dto.response.PageResponse
import com.example.boardproject_kt.usecase.comment.CommentReadUseCase
import com.example.boardproject_kt.usecase.comment.CommentWriteUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/comment")
class CommentController(
    private val commentReadUseCase: CommentReadUseCase,
    private val commentWriteUseCase: CommentWriteUseCase,
) {

    @GetMapping("/board")
    fun getBoardCommentList(
        @RequestParam(value = "id", required = false) @Min(value = 1) id: Long,
        @RequestParam(value = "page", required = false) @Min(value = 1) page: Int
    ): ResponseEntity<ApiResponse<PageResponse<BoardCommentResponse>>> {
        val body = commentReadUseCase.getBoardCommentList(id, page)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @GetMapping("/image-board")
    fun getImageBoardCommentList(
        @RequestParam(value = "id", required = false) @Min(value = 1) id: Long,
        @RequestParam(value = "page", required = false) @Min(value = 1) page: Int
    ): ResponseEntity<ApiResponse<PageResponse<BoardCommentResponse>>> {
        val body = commentReadUseCase.getImageBoardCommentList(id, page)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @PostMapping("/board/{targetBoardId}")
    @PreAuthorize("isAuthenticated()")
    fun postBoardComment(
        @PathVariable(name = "targetBoardId")
        @Min(value = 1)
        targetBoardId: Long,
        @RequestBody @Valid request: CommentRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        commentWriteUseCase.postBoardComment(targetBoardId, request, principal.name)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/image-board/{targetBoardId}")
    @PreAuthorize("isAuthenticated()")
    fun postImageBoardComment(
        @PathVariable(name = "targetBoardId")
        @Min(value = 1)
        targetBoardId: Long,
        @RequestBody @Valid request: CommentRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        commentWriteUseCase.postImageBoardComment(targetBoardId, request, principal.name)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteComment(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        principal: Principal
    ): ResponseEntity<Unit> {
        commentWriteUseCase.deleteComment(id, principal.name)

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/reply")
    @PreAuthorize("isAuthenticated()")
    fun postReplyComment(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        @RequestBody @Valid request: CommentRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        commentWriteUseCase.postReplyComment(id, request, principal.name)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}