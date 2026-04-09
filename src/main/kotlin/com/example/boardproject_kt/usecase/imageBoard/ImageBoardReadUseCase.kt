package com.example.boardproject_kt.usecase.imageBoard

import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.example.boardproject_kt.domain.dto.common.request.ListRequest
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardDetailResponse
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardListResponse
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardPatchDetailResponse
import com.example.boardproject_kt.domain.dto.response.PageResponse
import com.example.boardproject_kt.domain.enums.ListAmount
import com.example.boardproject_kt.exception.CustomNotFoundException
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.service.common.PrincipalService
import com.example.boardproject_kt.service.imageBoard.ImageBoardDataService
import com.example.boardproject_kt.service.imageBoard.ImageDataDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {  }

@Service
class ImageBoardReadUseCase(
    private val imageBoardDataService: ImageBoardDataService,
    private val imageDataDataService: ImageDataDataService,
    private val principalService: PrincipalService,
) {

    fun getImageBoardList(request: ListRequest): PageResponse<ImageBoardListResponse> {
        val condition = PageCondition.from(request)
        val pageable = PageRequest.of(
            request.page!! - 1,
            ListAmount.IMAGE_BOARD.key
        )

        val content = imageBoardDataService.findAllListByPageable(condition, pageable)

        return PageResponse.of(content)
    }

    fun getImageBoardDetail(id: Long): ImageBoardDetailResponse {
        val imageBoard = imageBoardDataService.findDetailById(id)

        if(imageBoard == null){
            log.warn { "ImageBoardReadUseCase.getImageBoardDetail :: imageBoard is null. id=$id" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST)
        }

        val imageData = imageDataDataService.getImageDataNameList(id)

        return ImageBoardDetailResponse.of(imageBoard, imageData)
    }

    fun getPatchData(id: Long, userId: String): ImageBoardPatchDetailResponse {
        val imageBoard = imageBoardDataService.findPatchDetailById(id)

        if(imageBoard == null){
            log.warn { "ImageBoardReadUseCase.getPatchData :: imageBoard is null. id=$id" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST)
        }

        principalService.validateUser(imageBoard.writer, userId)
        val imageDataList = imageDataDataService.getImageDataList(id)

        return ImageBoardPatchDetailResponse(
            title = imageBoard.title,
            content = imageBoard.content,
            imageList = imageDataList
        )
    }
}