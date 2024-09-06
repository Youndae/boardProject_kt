package com.example.boardproject_kt_ver_default.domain.mapper

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import org.springframework.stereotype.Component

@Component
class PaginationRequestMapper {

    fun toPaginationDTO(pageNum: Int): PaginationDTO {
        return PaginationDTO(pageNum)
    }

    fun toPaginationDTO(pageNum: Int, keyword: String?, searchType: String?): PaginationDTO {
        return PaginationDTO(pageNum, keyword, searchType)
    }
}