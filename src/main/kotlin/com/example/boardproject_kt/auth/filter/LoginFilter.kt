package com.example.boardproject_kt.auth.filter

import com.example.boardproject_kt.auth.user.domain.LoginRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException

class LoginFilter(
    private val om: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        if(request.method != "POST")
            throw AuthenticationServiceException("Authentication method not post: ${request.method}")

        try {
            val loginRequest: LoginRequest = om.readValue<LoginRequest>(request.inputStream, )
            val authenticationToken = UsernamePasswordAuthenticationToken(
                loginRequest.userId,
                loginRequest.password
            )

            return this.authenticationManager.authenticate(authenticationToken)
        }catch(e: IOException) {
            throw AuthenticationServiceException("Failed to parse loginRequest body", e)
        }
    }
}