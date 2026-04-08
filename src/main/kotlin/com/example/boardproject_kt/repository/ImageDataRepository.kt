package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.entity.ImageData
import org.springframework.data.jpa.repository.JpaRepository

interface ImageDataRepository : JpaRepository<ImageData, String>, CustomImageDataRepository {
}