package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.example.boardproject_kt_ver_default.domain.entity.ImageBoard
import org.springframework.data.jpa.repository.JpaRepository

interface ImageBoardRepository: JpaRepository<ImageBoard, Long>, ImageBoardCustomRepository {
}