package com.example.boardproject_kt_ver_default.service.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.business.imageBoard.FileDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.imageBoard.ImageBoardPostDTO
import com.example.boardproject_kt_ver_default.domain.entity.ImageBoard
import com.example.boardproject_kt_ver_default.domain.entity.ImageData
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import com.example.boardproject_kt_ver_default.repository.imageBoard.ImageBoardRepository
import com.example.boardproject_kt_ver_default.repository.imageBoard.ImageDataRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

import com.example.boardproject_kt_ver_default.domain.enumuration.Result

@Service
class ImageBoardWriteService(
    private val imageBoardRepository: ImageBoardRepository,
    private val imageDataRepository: ImageDataRepository
) {
    fun postBoard(imageBoard: ImageBoard, fileNameList: List<FileDTO>): Long {
        addImageData(imageBoard, fileNameList, 1)

        return imageBoardRepository.save(imageBoard).imageNo
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteBoard(imageNo: Long, principal: Principal): String {
        val imageBoard = imageBoardRepository.findById(imageNo)
                                .orElseThrow { NoSuchElementException("delete imageBoard not found") }

        if(imageBoard.member.userId != principal.name)
            throw CustomAccessDeniedException("delete imageBoard AccessDenied")

        imageBoardRepository.deleteById(imageNo)

        return Result.SUCCESS.resultMessage
    }

    fun patchBoard(dto: ImageBoardPostDTO
                   , member: Member
                   , fileNameList: List<FileDTO>?
                   , deleteFiles: List<String>?
                   , imageNo: Long): Long {
        var imageBoard = imageBoardRepository.findById(imageNo)
                                .orElseThrow { NoSuchElementException("patch imageBoard not found") }

        if(imageBoard.member.userId != member.userId)
            throw CustomAccessDeniedException("patch imageBoard AccessDenied")

        imageBoard.setPatchData(dto)

        val lastStep: Int = imageDataRepository.findByLastStep(imageNo)

        if(fileNameList != null)
            addImageData(imageBoard, fileNameList, lastStep + 1)

        if(deleteFiles != null)
            imageDataRepository.deleteAllById(deleteFiles)

        return imageBoardRepository.save(imageBoard).imageNo

    }

    private fun addImageData(imageBoard: ImageBoard, fileNameList: List<FileDTO>, step: Int) {
        var imageStep = step

        fileNameList.forEach {
            imageBoard.addImageData(
                ImageData(
                    imageName = it.imageName,
                    oldName = it.oldName,
                    imageStep = imageStep++
                )
            )
        }
    }
}