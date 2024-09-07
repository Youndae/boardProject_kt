package com.example.boardproject_kt_ver_default.repository.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination.PaginationDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ImageBoardCustomRepository {

    fun findListToPageable(pageable: Pageable, pageDTO: PaginationDTO): Page<ImageBoardListDTO>


}