package com.example.boardproject_kt.auth.handler

import com.example.boardproject_kt.auth.service.TokenProvider
import com.example.boardproject_kt.domain.dto.member.response.MemberStatusResponse
import com.example.boardproject_kt.domain.dto.response.ApiResponse
import com.example.boardproject_kt.properties.CookieProperties
import com.example.boardproject_kt.service.member.AuthContextService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils

@Component
class LoginSuccessHandler(
    private val om: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val authContextService: AuthContextService,
    private val cookieProperties: CookieProperties
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val responseContent: MemberStatusResponse = authContextService.getMemberStatus(authentication)
        val inoCookie = WebUtils.getCookie(request, cookieProperties.ino.header)
        val userId = responseContent.userId

        if(inoCookie == null)
            tokenProvider.issuedAllToken(userId, response)
        else
            tokenProvider.issuedToken(userId, inoCookie.value, response)

        val body = ApiResponse.success<MemberStatusResponse>(responseContent)

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            status = HttpServletResponse.SC_OK
            writer.write(om.writeValueAsString(body))
        }
    }
}