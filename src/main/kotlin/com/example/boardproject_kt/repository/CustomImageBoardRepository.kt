package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.common.business.PageCondition
import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardDetail
import com.example.boardproject_kt.domain.dto.imageBoard.business.ImageBoardPatchDetail
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardListResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomImageBoardRepository {

    fun findAllListByPageable(pageCondition: PageCondition, pageable: Pageable): Page<ImageBoardListResponse>

    fun findDetailById(id: Long): ImageBoardDetail?

    fun findPatchDetailById(id: Long): ImageBoardPatchDetail?
}