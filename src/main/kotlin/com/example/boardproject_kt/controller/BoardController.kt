package com.example.boardproject_kt.controller

import com.example.boardproject_kt.domain.dto.board.request.BoardReplyRequest
import com.example.boardproject_kt.domain.dto.board.request.BoardRequest
import com.example.boardproject_kt.domain.dto.board.response.BoardDetailResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardListResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardPatchDetailResponse
import com.example.boardproject_kt.domain.dto.common.request.ListRequest
import com.example.boardproject_kt.domain.dto.response.ApiResponse
import com.example.boardproject_kt.domain.dto.response.PageResponse
import com.example.boardproject_kt.usecase.board.BoardReadUseCase
import com.example.boardproject_kt.usecase.board.BoardWriteUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/api/board")
class BoardController(
    private val boardReadUseCase: BoardReadUseCase,
    private val boardWriteUseCase: BoardWriteUseCase
) {

    @GetMapping("")
    fun getBoardList(
        listRequest: ListRequest
    ): ResponseEntity<ApiResponse<PageResponse<BoardListResponse>>> {
        listRequest.validate()
        val body = boardReadUseCase.getBoardList(listRequest)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @GetMapping("/{id}")
    fun getDetail(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long
    ): ResponseEntity<ApiResponse<BoardDetailResponse>> {
        val body = boardReadUseCase.getBoardDetail(id)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    fun postBoard(
        @RequestBody @Valid request: BoardRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Long>> {
        val body: Long = boardWriteUseCase.postBoard(request, principal.name)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(body))
    }

    @GetMapping("/patch-detail/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getPatchDetail(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        principal: Principal
    ): ResponseEntity<ApiResponse<BoardPatchDetailResponse>> {
        val body = boardReadUseCase.getPatchData(id, principal.name)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun patchBoard(
        @RequestBody @Valid request: BoardRequest,
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        principal: Principal
    ): ResponseEntity<ApiResponse<Long>> {
        val body: Long = boardWriteUseCase.patchBoard(request, id, principal.name)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteBoard(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        principal: Principal
    ): ResponseEntity<Unit> {
        boardWriteUseCase.deleteBoard(id, principal.name)

        return ResponseEntity.noContent().build()
    }

    @GetMapping("/reply/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getReplyDetail(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long
    ): ResponseEntity<Unit> {
        boardReadUseCase.getReplyInfo(id)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/reply/{id}")
    @PreAuthorize("isAuthenticated()")
    fun postReply(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        @RequestBody @Valid request: BoardReplyRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Long>> {
        val body: Long = boardWriteUseCase.postBoardReply(id, request, principal.name)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(body))
    }
}