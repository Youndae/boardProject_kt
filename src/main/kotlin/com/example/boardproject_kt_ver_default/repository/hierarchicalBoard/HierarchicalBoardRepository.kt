package com.example.boardproject_kt_ver_default.repository.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.entity.HierarchicalBoard
import org.springframework.data.jpa.repository.JpaRepository

interface HierarchicalBoardRepository: JpaRepository<HierarchicalBoard, Long>, HierarchicalBoardCustomRepository {
}