package com.example.boardproject_kt_ver_default.repository.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardReplyInfoDTO
import com.example.boardproject_kt_ver_default.domain.entity.HierarchicalBoard
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HierarchicalBoardCustomRepository {

    fun findListToPageable(pageable: Pageable, pageDTO: PaginationDTO): Page<HierarchicalBoardListDTO>

    fun findBoardDetailByBoardNo(boardNo: Long): HierarchicalBoardDetailDTO?

    fun getPatchData(boardNo: Long): HierarchicalBoardPatchDTO?

    fun findReplyInfoByBoardNo(boardNo: Long): HierarchicalBoardReplyInfoDTO?

    fun deleteByBoardGroupNo(boardNo: Long)

    fun findAllByBoardGroupNo(groupNo: Long): List<HierarchicalBoard>
}