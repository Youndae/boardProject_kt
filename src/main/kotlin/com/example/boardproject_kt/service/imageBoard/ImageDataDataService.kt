package com.example.boardproject_kt.service.imageBoard

import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageDataResponse
import com.example.boardproject_kt.domain.entity.ImageData
import com.example.boardproject_kt.repository.ImageDataRepository
import org.springframework.stereotype.Service

@Service
class ImageDataDataService(
    private val imageDataRepository: ImageDataRepository
) {

    fun getImageDataNameList(id: Long): List<String> =
            imageDataRepository.getImageDataNameList(id)

    fun saveAllEntity(imageDataList: List<ImageData>) =
            imageDataRepository.saveAll(imageDataList)

    fun getImageDataList(id: Long): List<ImageDataResponse> =
            imageDataRepository.getImageDataList(id)

    fun countImageStep(id: Long): Int =
            imageDataRepository.countImageStep(id)

    fun deleteImageDataList(deleteFiles: List<String>) =
            imageDataRepository.deleteAllByIdInBatch(deleteFiles)

    fun deleteImageDataListByImageBoardId(id: Long) =
            imageDataRepository.deleteImageDataByImageBoardId(id)
}