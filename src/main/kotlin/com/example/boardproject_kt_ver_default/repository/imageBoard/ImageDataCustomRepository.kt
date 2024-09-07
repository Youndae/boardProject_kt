package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageDataDTO

interface ImageDataCustomRepository {

    fun getImageData(imageNo: Long): List<ImageDataDTO>

    fun findByLastStep(imageNo: Long): Int
}