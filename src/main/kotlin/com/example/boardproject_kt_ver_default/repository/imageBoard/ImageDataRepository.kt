package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.example.boardproject_kt_ver_default.domain.entity.ImageData
import org.springframework.data.jpa.repository.JpaRepository

interface ImageDataRepository: JpaRepository<ImageData, String>, ImageDataCustomRepository {
}