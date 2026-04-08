package com.example.boardproject_kt.usecase.imageBoard

import com.example.boardproject_kt.domain.dto.imageBoard.request.ImageBoardRequest
import com.example.boardproject_kt.domain.entity.ImageData
import com.example.boardproject_kt.service.common.PrincipalService
import com.example.boardproject_kt.service.file.FileDataService
import com.example.boardproject_kt.service.imageBoard.ImageBoardDataService
import com.example.boardproject_kt.service.imageBoard.ImageDataDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

private val log = KotlinLogging.logger {  }

@Service
class ImageBoardWriteUseCase(
    private val imageBoardDataService: ImageBoardDataService,
    private val imageDataDataService: ImageDataDataService,
    private val fileDataService: FileDataService,
    private val principalService: PrincipalService
) {

    @Transactional(rollbackFor = [Exception::class])
    fun postBoard(
        images: List<MultipartFile>,
        request: ImageBoardRequest,
        userId: String
    ): Long {
        if(images.isEmpty() || images.any{ it.isEmpty })
            throw IllegalArgumentException("image list cannot be empty")

        val memberEntity = principalService.getMemberByUserId(userId)
        val imageBoardEntity = request.toEntity(memberEntity)
        val imageDataList: List<ImageData> = fileDataService.boardImageInsert(images, 1)

        val imageBoard = imageBoardDataService.saveEntity(imageBoardEntity)
        imageDataList.forEach { it.updateImageBoard(imageBoard) }
        imageDataDataService.saveAllEntity(imageDataList)

        return imageBoard.id!!
    }

    @Transactional(rollbackFor = [Exception::class])
    fun patchBoard(
        images: List<MultipartFile>?,
        deleteFiles: List<String>?,
        id: Long,
        request: ImageBoardRequest,
        userId: String
    ): Long {
        val imageBoard = imageBoardDataService.findById(id)
        principalService.validateUser(imageBoard.member.userId, userId)
        imageBoard.updateImageBoardData(request)

        if(images != null){
            val maxStep = imageDataDataService.countImageStep(id)
            val saveImageData = fileDataService.boardImageInsert(images, maxStep + 1)
            saveImageData.forEach { it.updateImageBoard(imageBoard) }
            imageDataDataService.saveAllEntity(saveImageData)
        }

        if(deleteFiles != null) {
            fileDataService.deleteImageBoardFile(deleteFiles)
            imageDataDataService.deleteImageDataList(deleteFiles)
        }

        return id
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteBoard(id: Long, userId: String) {
        val imageBoard = imageBoardDataService.findById(id)
        principalService.validateUser(imageBoard.member.userId, userId)

        val deleteFiles = imageDataDataService.getImageDataNameList(id)
        fileDataService.deleteImageBoardFile(deleteFiles)
        imageDataDataService.deleteImageDataListByImageBoardId(id)
        imageBoardDataService.deleteById(id)
    }
}