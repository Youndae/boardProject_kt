package com.example.boardproject_kt_ver_default.service.hierarchicalBoard


import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardReplyInfoDTO
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import com.example.boardproject_kt_ver_default.repository.hierarchicalBoard.HierarchicalBoardRepository
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class HierarchicalBoardReadService(
    private val boardRepository: HierarchicalBoardRepository
) {

    private val log = logger()

    fun getList(pageDTO: PaginationDTO): Page<HierarchicalBoardListDTO> {
        var pageable: Pageable = PageRequest.of(pageDTO.pageNum - 1
                                                , pageDTO.boardAmount
                                                , Sort.by("boardGroupNo").descending()
                                                    .and(Sort.by("boardUpperNo").ascending())
                                            );

        return boardRepository.findListToPageable(pageable, pageDTO)
    }

    fun getDetail(boardNo: Long): HierarchicalBoardDetailDTO {

        return boardRepository.findBoardDetailByBoardNo(boardNo) ?:
                            throw NoSuchElementException("Detail data not found")
    }

    fun getPatchDetail(boardNo: Long, principal: Principal): HierarchicalBoardPatchDTO {
        val writer: String = boardRepository.findById(boardNo)
            .orElseThrow { IllegalArgumentException("board patch data not found") }
            .member.userId

        if (writer != principal.name)
            throw CustomAccessDeniedException("patchData writer AccessDenied")

        return boardRepository.getPatchData(boardNo) ?:
                            throw NoSuchElementException("patchDetail data not found")
    }

    fun getReplyInfo(boardNo: Long): HierarchicalBoardReplyInfoDTO {

        return boardRepository.findReplyInfoByBoardNo(boardNo) ?:
                            throw NoSuchElementException("ReplyInfo data not found")
    }


}