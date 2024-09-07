package com.example.boardproject_kt_ver_default.service.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageDataDTO
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import com.example.boardproject_kt_ver_default.repository.imageBoard.ImageBoardRepository
import com.example.boardproject_kt_ver_default.repository.imageBoard.ImageDataRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.security.Principal

@Service
class ImageBoardReadService(
    private val imageBoardRepository: ImageBoardRepository,
    private val imageDataRepository: ImageDataRepository
) {

    fun getList(pageDTO: PaginationDTO): Page<ImageBoardListDTO> {
        val pageable: Pageable = PageRequest.of(pageDTO.pageNum - 1
                                    , pageDTO.imageAmount
                                    , Sort.by("imageNo").descending()
                                )

        return imageBoardRepository.findListToPageable(pageable, pageDTO)
    }

    fun getDetail(imageNo: Long): ImageBoardDetailDTO {
        val imageBoard = imageBoardRepository.findById(imageNo)
            .orElseThrow { NoSuchElementException("imageBoard detail not found") }

        val dataDTO: List<ImageDataDTO> = imageDataRepository.getImageData(imageNo)

        return ImageBoardDetailDTO(imageBoard, dataDTO)
    }

    fun getPatchDetail(imageNo: Long, principal: Principal): ImageBoardPatchDTO {
        val imageBoard = imageBoardRepository.findById(imageNo)
                                    .orElseThrow { NoSuchElementException("imageBoard patch detail not found") }

        if(imageBoard.member.userId != principal.name)
            throw CustomAccessDeniedException("imageBoard patch detail AccessDenied")

        return ImageBoardPatchDTO(imageBoard)
    }

    fun getPatchImage(imageNo: Long): List<ImageDataDTO> {

        return imageDataRepository.getImageData(imageNo)
    }




}