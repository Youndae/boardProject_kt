package com.example.boardproject_kt.auth.filter

import com.example.boardproject_kt.auth.oAuth.CustomOAuth2User
import com.example.boardproject_kt.auth.service.TokenProvider
import com.example.boardproject_kt.auth.user.CustomUser
import com.example.boardproject_kt.auth.user.CustomUserDetails
import com.example.boardproject_kt.domain.entity.Member
import com.example.boardproject_kt.domain.enumuration.OAuthProvider
import com.example.boardproject_kt.domain.enumuration.TokenValidationResult
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.properties.CookieProperties
import com.example.boardproject_kt.properties.TokenProperties
import com.example.boardproject_kt.repository.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils

class JwtAuthorizationFilter(
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: TokenProvider,
    private val tokenProperties: TokenProperties,
    private val cookieProperties: CookieProperties
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessTokenCookie: Cookie? = WebUtils.getCookie(request, tokenProperties.access.header)
        val refreshTokenCookie: Cookie? = WebUtils.getCookie(request, tokenProperties.refresh.header)
        val inoCookie: Cookie? = WebUtils.getCookie(request, cookieProperties.ino.header)
        var username: String? = null

        if(inoCookie != null) {
            if(accessTokenCookie != null && refreshTokenCookie != null) {
                if(!accessTokenCookie.value.startsWith(tokenProperties.prefix)
                    || !refreshTokenCookie.value.startsWith(tokenProperties.prefix)) {
                    filterChain.doFilter(request, response)
                    return
                }else {
                    val inoValue = inoCookie.value
                    var claimByUserIdToAccessToken = jwtTokenProvider.verifyAccessToken(accessTokenCookie, inoValue)

                    if(claimByUserIdToAccessToken == TokenValidationResult.TOKEN_STEALING.result
                        || claimByUserIdToAccessToken == TokenValidationResult.WRONG_TOKEN.result) {
                        deleteTokenCookieThrowException(response)
                        return
                    }else if(claimByUserIdToAccessToken == TokenValidationResult.TOKEN_EXPIRATION.result) {
                        claimByUserIdToAccessToken = jwtTokenProvider.decodeToken(accessTokenCookie)
                        val verifyRefreshTokenResult = jwtTokenProvider.verifyRefreshToken(
                            refreshTokenCookie,
                            inoValue,
                            claimByUserIdToAccessToken
                        )

                        if(verifyRefreshTokenResult == claimByUserIdToAccessToken) {
                            jwtTokenProvider.issuedToken(claimByUserIdToAccessToken, inoValue, response)
                            username = claimByUserIdToAccessToken
                        }else if(verifyRefreshTokenResult == TokenValidationResult.TOKEN_STEALING.result
                            || verifyRefreshTokenResult == TokenValidationResult.WRONG_TOKEN.result) {
                            deleteTokenAndCookieThrowException(claimByUserIdToAccessToken, inoValue, response)
                            return
                        }
                    }else {
                        username = claimByUserIdToAccessToken
                    }
                }
            }else if(accessTokenCookie == null && refreshTokenCookie == null) {
                filterChain.doFilter(request, response)
                return
            }else {
                var claimByUserId: String

                if(accessTokenCookie != null)
                    claimByUserId = jwtTokenProvider.decodeToken(accessTokenCookie)
                else
                    claimByUserId = jwtTokenProvider.decodeToken(refreshTokenCookie!!)

                deleteTokenAndCookieThrowException(claimByUserId, inoCookie.value, response)
                return
            }
        }

        username?.let { name ->
            val memberEntity: Member? = memberRepository.findByUserId(name)

            if(memberEntity == null)
                deleteTokenAndCookieThrowException(username, inoCookie!!.value, response)



            val userDetails: CustomUserDetails =
                if(memberEntity!!.provider == OAuthProvider.LOCAL.key) {
                    CustomUser(memberEntity)
                } else {
                    CustomOAuth2User(memberEntity.toOAuth2DTOUseFilter())
                }

            val authentication = UsernamePasswordAuthenticationToken(
                userDetails.userId,
                null,
                userDetails.getAuthorities()
            )

            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun tokenStealingExceptionResponse(response: HttpServletResponse) {
        response.apply {
            status = ErrorCode.TOKEN_STEALING.httpStatus.value()
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = "utf-8"
        }
    }

    private fun deleteTokenAndCookieThrowException(
        tokenClaim: String,
        inoValue: String,
        response: HttpServletResponse
    ) {
        jwtTokenProvider.deleteToken(tokenClaim, inoValue, response)
        tokenStealingExceptionResponse(response)
    }

    private fun deleteTokenCookieThrowException(response: HttpServletResponse) {
        jwtTokenProvider.deleteTokenCookie(response)
        tokenStealingExceptionResponse(response)
    }
}