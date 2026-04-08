package com.example.boardproject_kt.auth.handler

import com.example.boardproject_kt.domain.dto.response.ExceptionResponse
import com.example.boardproject_kt.domain.enumuration.ResponseStatus
import com.example.boardproject_kt.exception.ErrorCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class LoginFailureHandler(private val om: ObjectMapper) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val body: ExceptionResponse<Unit> = ExceptionResponse(
            code = ErrorCode.UNAUTHORIZED.httpStatus.value(),
            message = ResponseStatus.FAIL.message
        )

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = HttpServletResponse.SC_UNAUTHORIZED
            characterEncoding = "UTF-8"

            writer.write(om.writeValueAsString(body))
        }
    }
}