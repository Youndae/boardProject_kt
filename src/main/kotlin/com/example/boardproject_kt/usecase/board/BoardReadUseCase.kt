package com.example.boardproject_kt.usecase.board

import com.example.boardproject_kt.domain.dto.board.response.BoardDetailResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardListResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardPatchDetailResponse
import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.example.boardproject_kt.domain.dto.common.request.ListRequest
import com.example.boardproject_kt.domain.dto.response.PageResponse
import com.example.boardproject_kt.domain.enums.ListAmount
import com.example.boardproject_kt.exception.CustomNotFoundException
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.service.board.BoardDataService
import com.example.boardproject_kt.service.common.PrincipalService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {  }

@Service
class BoardReadUseCase(
    private val boardDataService: BoardDataService,
    private val principalService: PrincipalService
) {

    fun getBoardList(request: ListRequest): PageResponse<BoardListResponse> {
        val condition = PageCondition.from(request)
        val pageable = PageRequest.of(
            request.page!! - 1,
            ListAmount.BOARD.key
        )

        val content: Page<BoardListResponse> = boardDataService.findAllListByPageable(condition, pageable)

        return PageResponse.of(content)
    }

    fun getBoardDetail(id: Long): BoardDetailResponse {
        val content = boardDataService.findDetailResponseById(id)

        if(content == null){
            log.warn { "Board detail content is null. board id=$id" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST)
        }

        return content
    }

    fun getPatchData(id: Long, userId: String): BoardPatchDetailResponse {
        val patchDetail = boardDataService.findPatchDetailById(id)

        if(patchDetail == null){
            log.warn { "Board patch detail content is null. board id=$id" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST)
        }

        principalService.validateUser(patchDetail.userId, userId)

        return BoardPatchDetailResponse(
            title = patchDetail.title,
            content = patchDetail.content
        )
    }

    fun getReplyInfo(id: Long) {
        boardDataService.findById(id)
    }
}