package com.example.boardproject_kt_ver_default.domain.factory

import com.example.boardproject_kt_ver_default.domain.dto.out.comment.CommentDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.response.ResponsePageableListDTO
import com.example.boardproject_kt_ver_default.service.auth.PrincipalReadService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class ResponseFactory(
    private val principalService: PrincipalReadService
) {

    fun createStringResponse(result: String): ResponseEntity<String> {

        return ResponseEntity.status(HttpStatus.OK)
                .body(result)
    }

    fun createLongResponse(result: Long): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.OK)
                .body(result)
    }

    fun <T> createPaginationList(
        result: Page<T>,
        principal: Principal
    ): ResponseEntity<ResponsePageableListDTO<T>> {
        val nickname = principalService.getNicknameToPrincipal(principal)

        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ResponsePageableListDTO(result, nickname)
            )
    }
}