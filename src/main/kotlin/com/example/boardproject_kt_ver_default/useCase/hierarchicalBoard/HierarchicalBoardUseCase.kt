package com.example.boardproject_kt_ver_default.useCase.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.out.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.service.hierarchicalBoard.HierarchicalBoardReadService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class HierarchicalBoardUseCase(
    private val boardService: HierarchicalBoardReadService
) {

    fun getList(): Page<HierarchicalBoardListDTO> {

        return boardService.getList()
    }
}