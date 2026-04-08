package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageDataResponse

interface CustomImageDataRepository {

    fun getImageDataNameList(id: Long): List<String>

    fun getImageDataList(id: Long): List<ImageDataResponse>

    fun countImageStep(id: Long): Int

    fun deleteImageDataByImageBoardId(id: Long)
}