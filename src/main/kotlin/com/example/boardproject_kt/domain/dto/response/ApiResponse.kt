package com.example.boardproject_kt.domain.dto.response

import com.example.boardproject_kt.domain.enums.ResponseStatus
import org.springframework.http.HttpStatus
import java.time.OffsetDateTime

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val content: T? = null,
    val timestamp: OffsetDateTime = OffsetDateTime.now()
) {

    companion object {
        fun <T> success(
            content: T,
            message: String = ResponseStatus.SUCCESS.message
        ): ApiResponse<T> = ApiResponse(
                code = HttpStatus.OK.value(),
                message = message,
                content = content
            )

        fun <T> success(message: String) : ApiResponse<T> = ApiResponse(
            code = HttpStatus.OK.value(),
            message = message,
        )

        fun <T> created(
            content: T,
            message: String = ResponseStatus.SUCCESS.message
        ): ApiResponse<T> = ApiResponse(
            code = HttpStatus.CREATED.value(),
            message = message,
            content = content
        )
    }
}
