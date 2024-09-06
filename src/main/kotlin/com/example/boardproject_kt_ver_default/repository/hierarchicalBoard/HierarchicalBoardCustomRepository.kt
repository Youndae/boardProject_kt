package com.example.boardproject_kt_ver_default.repository.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.out.HierarchicalBoardListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HierarchicalBoardCustomRepository {

    fun findListToPageable(pageable: Pageable): Page<HierarchicalBoardListDTO>
}