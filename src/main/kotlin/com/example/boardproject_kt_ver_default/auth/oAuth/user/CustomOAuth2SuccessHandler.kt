package com.example.boardproject_kt_ver_default.auth.oAuth.user

import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtProperties
import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtTokenProvider
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import java.io.IOException

@Component
class CustomOAuth2SuccessHandler(
    private val tokenProvider: JwtTokenProvider
): SimpleUrlAuthenticationSuccessHandler() {

    @kotlin.jvm.Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val customOAuth2User: CustomOAuth2User = authentication.principal as CustomOAuth2User
        val userId = customOAuth2User.getUserId()
        val authorities: Collection<out GrantedAuthority> = authentication.authorities
        val iterator: Iterator<out GrantedAuthority> = authorities.iterator()
        val auth: GrantedAuthority = iterator.next()
        val inoCookie: Cookie? = WebUtils.getCookie(request, JwtProperties.INO_HEADER)

        if(inoCookie == null)
            tokenProvider.issuedAllToken(userId, response)
        else
            tokenProvider.issuedToken(userId, inoCookie.value, response)

        if(customOAuth2User.getNickname() == null)
            response.sendRedirect("http://localhost:3000/member/profile")
        else
            response.sendRedirect("http://localhost:3000/member/oAuth")
    }
}