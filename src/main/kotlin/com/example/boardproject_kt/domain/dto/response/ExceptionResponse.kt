package com.example.boardproject_kt.domain.dto.response

import com.example.boardproject_kt.domain.enums.ResponseStatus
import com.example.boardproject_kt.exception.ErrorCode
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.OffsetDateTime

data class ExceptionResponse<T> (
    val code: Int,
    val message: String,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val errors: List<T>? = null,

    val timestamp: OffsetDateTime = OffsetDateTime.now()
) {
    companion object {
        fun <T> exception(
            errorCode: ErrorCode,
            message: String = ResponseStatus.FAIL.message
        ): ExceptionResponse<T> = ExceptionResponse(
            code = errorCode.httpStatus.value(),
            message = message
        )

        fun <T> validationException(
            errorCode: ErrorCode,
            errors: List<T>,
            message: String = ResponseStatus.FAIL.message
        ) : ExceptionResponse<T> = ExceptionResponse(
            code = errorCode.httpStatus.value(),
            errors = errors,
            message = message
        )
    }
}
