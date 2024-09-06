package com.example.boardproject_kt_ver_default.service.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.out.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.repository.hierarchicalBoard.HierarchicalBoardRepository
import com.example.boardproject_kt_ver_default.util.logger
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class HierarchicalBoardReadService(
    private val boardRepository: HierarchicalBoardRepository
) {

    private val log = logger()

    fun getList(): Page<HierarchicalBoardListDTO> {
        var pageable: Pageable = PageRequest.of(0, 20, Sort.by("boardGroupNo").descending()
            .and(Sort.by("boardUpperNo").ascending()));

        var result = boardRepository.findListToPageable(pageable)

        log.info("$result")

        return result
    }
}