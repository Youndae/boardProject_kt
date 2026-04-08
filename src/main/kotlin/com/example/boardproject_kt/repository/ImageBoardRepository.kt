package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.entity.ImageBoard
import org.springframework.data.jpa.repository.JpaRepository

interface ImageBoardRepository : JpaRepository<ImageBoard, Long>, CustomImageBoardRepository {
}