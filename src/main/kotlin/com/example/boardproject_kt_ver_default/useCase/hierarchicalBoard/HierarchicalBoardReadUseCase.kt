package com.example.boardproject_kt_ver_default.useCase.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardReplyInfoDTO
import com.example.boardproject_kt_ver_default.domain.mapper.PaginationRequestMapper
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import com.example.boardproject_kt_ver_default.service.auth.PrincipalReadService
import com.example.boardproject_kt_ver_default.service.hierarchicalBoard.HierarchicalBoardReadService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class HierarchicalBoardReadUseCase(
    private val boardReadService: HierarchicalBoardReadService,
    private val paginationRequestMapper: PaginationRequestMapper,
    private val principalReadService: PrincipalReadService
) {
    fun getList(pageNum: Int, keyword: String?, searchType: String?): Page<HierarchicalBoardListDTO> {
        val pageDTO: PaginationDTO = paginationRequestMapper.toPaginationDTO(pageNum, keyword, searchType)

        return boardReadService.getList(pageDTO)
    }

    fun getDetail(boardNo: Long): HierarchicalBoardDetailDTO {

        return boardReadService.getDetail(boardNo)
    }

    fun getPatchDetail(boardNo: Long, principal: Principal): HierarchicalBoardPatchDTO {

        return boardReadService.getPatchDetail(boardNo, principal)
    }

    fun getReplyData(boardNo: Long): HierarchicalBoardReplyInfoDTO {

        return boardReadService.getReplyInfo(boardNo)
    }
}