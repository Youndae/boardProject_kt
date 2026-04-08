package com.example.boardproject_kt.service.imageBoard

import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardDetail
import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardPatchDetail
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardListResponse
import com.example.boardproject_kt.domain.entity.ImageBoard
import com.example.boardproject_kt.repository.ImageBoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ImageBoardDataService(
    private val imageBoardRepository: ImageBoardRepository
) {

    fun findAllListByPageable(pageCondition: PageCondition, pageable: Pageable): Page<ImageBoardListResponse> =
            imageBoardRepository.findAllListByPageable(pageCondition, pageable)

    fun findDetailById(id: Long): ImageBoardDetail? =
            imageBoardRepository.findDetailById(id)

    fun saveEntity(imageBoard: ImageBoard): ImageBoard =
            imageBoardRepository.save(imageBoard)

    fun findPatchDetailById(id: Long): ImageBoardPatchDetail? =
            imageBoardRepository.findPatchDetailById(id)

    fun findById(id: Long): ImageBoard =
            imageBoardRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Invalid patch image board id=$id") }

    fun deleteById(id: Long) =
            imageBoardRepository.deleteById(id)
}