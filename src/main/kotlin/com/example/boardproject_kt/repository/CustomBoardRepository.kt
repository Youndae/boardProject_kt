package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.board.business.BoardPatchDetail
import com.example.boardproject_kt.domain.dto.board.response.BoardDetailResponse
import com.example.boardproject_kt.domain.dto.board.response.BoardListResponse
import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomBoardRepository {

    fun findAllListByPageable(condition: PageCondition, pageable: Pageable): Page<BoardListResponse>

    fun findDetailResponseById(id: Long): BoardDetailResponse?

    fun findPatchDetailById(id: Long): BoardPatchDetail?

    fun deleteByGroupNo(id: Long)

    fun deleteByPath(groupNo: Long, selfUpperNo: String, childUpperNo: String)
}