package com.example.boardproject_kt.auth.oAuth

import com.example.boardproject_kt.auth.service.TokenProvider
import com.example.boardproject_kt.properties.CookieProperties
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
class CustomOAuthSuccessHandler(
    private val tokenProvider: TokenProvider,
    private val cookieProperties: CookieProperties
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val customOAuth2User: CustomOAuth2User = authentication.principal as CustomOAuth2User
        val userId = customOAuth2User.userId

        val redirectCookie = WebUtils.getCookie(request, "redirect_to")
        val redirectUrl = redirectCookie?.value ?: "/"
        val inoCookie = WebUtils.getCookie(request, cookieProperties.ino.header)


        redirectCookie?.apply {
            path = "/"
            maxAge = 0
            response.addCookie(this)
        }


        if(inoCookie == null)
            tokenProvider.issuedAllToken(userId, response)
        else
            tokenProvider.issuedToken(userId, inoCookie.value, response)

        val targetUrl = if(customOAuth2User.getNickname() == null)
                            "/join/profile?redirect=${URLEncoder.encode(redirectUrl, "UTF-8")}"
                        else
                            URLDecoder.decode(redirectUrl, StandardCharsets.UTF_8.toString())

        redirectStrategy.sendRedirect(request, response, "http://localhost:3000${targetUrl}")
    }
}