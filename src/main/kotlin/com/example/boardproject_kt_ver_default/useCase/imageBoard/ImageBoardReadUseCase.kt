package com.example.boardproject_kt_ver_default.useCase.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageDataDTO
import com.example.boardproject_kt_ver_default.domain.mapper.PaginationRequestMapper
import com.example.boardproject_kt_ver_default.service.imageBoard.ImageBoardReadService
import com.example.boardproject_kt_ver_default.service.imageBoard.file.ImageFileReadService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class ImageBoardReadUseCase(
    private val imageBoardReadService: ImageBoardReadService,
    private val fileReadService: ImageFileReadService,
    private val paginationRequestMapper: PaginationRequestMapper
) {
    fun getList(pageNum: Int, keyword: String?, searchType: String?): Page<ImageBoardListDTO> {
        val pageDTO = paginationRequestMapper.toPaginationDTO(pageNum, keyword, searchType)

        return imageBoardReadService.getList(pageDTO)
    }

    fun getDetail(imageNo: Long): ImageBoardDetailDTO {

        return imageBoardReadService.getDetail(imageNo)
    }

    fun getPatchDetail(imageNo: Long, principal: Principal): ImageBoardPatchDTO {

        return imageBoardReadService.getPatchDetail(imageNo, principal)
    }

    fun getPatchImage(imageNo: Long): List<ImageDataDTO> {

        return imageBoardReadService.getPatchImage(imageNo)
    }

    fun getFile(imageName: String): ResponseEntity<ByteArray> {

        return fileReadService.getFile(imageName)
    }


}