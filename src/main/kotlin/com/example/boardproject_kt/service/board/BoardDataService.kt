package com.example.boardproject_kt.service.board

import com.example.boardproject_kt.domain.dto.board.business.BoardPatchDetail
import com.example.boardproject_kt.domain.dto.board.response.BoardDetailResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardListResponse
import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.example.boardproject_kt.domain.entity.Board
import com.example.boardproject_kt.repository.BoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BoardDataService(
    private val boardRepository: BoardRepository
) {

    fun findAllListByPageable(pageCondition: PageCondition, pageable: Pageable): Page<BoardListResponse> =
                boardRepository.findAllListByPageable(pageCondition, pageable)

    fun findDetailResponseById(id: Long): BoardDetailResponse? = boardRepository.findDetailResponseById(id)

    fun saveBoard(board: Board): Board = boardRepository.save(board)

    fun findPatchDetailById(id: Long): BoardPatchDetail? = boardRepository.findPatchDetailById(id)

    fun findById(id: Long): Board {
        return boardRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Invalid patch board id=$id") }
    }

    fun deleteByGroupNo(id: Long) {
        boardRepository.deleteByGroupNo(id)
    }

    fun deleteByPath(groupNo: Long, selfUpperNo: String, childUpperNo: String) {
        boardRepository.deleteByPath(groupNo, selfUpperNo, childUpperNo)
    }


}