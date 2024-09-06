package com.example.boardproject_kt_ver_default.config.auth.jwt

import com.example.boardproject_kt_ver_default.auth.common.CustomUserDetails
import com.example.boardproject_kt_ver_default.auth.oAuth.domain.OAuth2DTO
import com.example.boardproject_kt_ver_default.auth.oAuth.user.CustomOAuth2User
import com.example.boardproject_kt_ver_default.auth.security.CustomUser
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.exception.domain.ErrorCode
import com.example.boardproject_kt_ver_default.repository.member.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils
import java.io.IOException
import java.util.Collections
import kotlin.jvm.Throws

@Component
class JwtAuthorizationFilter(
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider
): OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessTokenCookie: Cookie? = WebUtils.getCookie(request, JwtProperties.ACCESS_HEADER)
        val refreshTokenCookie: Cookie? = WebUtils.getCookie(request, JwtProperties.REFRESH_HEADER)
        val inoCookie: Cookie? = WebUtils.getCookie(request, JwtProperties.INO_HEADER)
        var username: String? = null

        if(inoCookie != null){
            if(accessTokenCookie != null && refreshTokenCookie != null) {
                if(!accessTokenCookie.value.startsWith(JwtProperties.TOKEN_PREFIX)
                    || !refreshTokenCookie.value.startsWith(JwtProperties.TOKEN_PREFIX)){
                    filterChain.doFilter(request, response)
                    return
                }else {
                    val inoValue:String = inoCookie.value
                    var claimByUserIdToAccessToken = jwtTokenProvider.verifyAccessToken(accessTokenCookie, inoValue)

                    if(claimByUserIdToAccessToken == JwtProperties.TOKEN_STEALING_RESPONSE
                        || claimByUserIdToAccessToken == JwtProperties.WRONG_TOKEN) {
                        deleteTokenCookieThrowException(response)
                        return
                    }else if(claimByUserIdToAccessToken == JwtProperties.TOKEN_EXPIRATION_RESPONSE){
                        claimByUserIdToAccessToken = jwtTokenProvider.decodeToken(accessTokenCookie)
                        val claimByUserIdToRefreshToken = jwtTokenProvider.verifyRefreshToken(
                                                            refreshTokenCookie
                                                            , inoValue
                                                            , claimByUserIdToAccessToken
                                                        )

                        if(claimByUserIdToRefreshToken == claimByUserIdToAccessToken) {
                            jwtTokenProvider.issuedToken(claimByUserIdToAccessToken, inoValue, response)
                            username = claimByUserIdToAccessToken
                        }else if(claimByUserIdToRefreshToken == JwtProperties.TOKEN_STEALING_RESPONSE
                            || claimByUserIdToRefreshToken == JwtProperties.WRONG_TOKEN) {
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

        if(username != null) {
            val memberEntity: Member = memberRepository.findByUserId(username)
            var userDetails: CustomUserDetails

            if(memberEntity.provider == "local")
                userDetails = CustomUser(memberEntity)
            else {
                val oAuth2DTO = OAuth2DTO(memberEntity)
                userDetails = CustomOAuth2User(oAuth2DTO)
            }

            val userId: String = userDetails.getUserId()
            val authorities: Collection<out GrantedAuthority> = userDetails.getAuthorities()
            val authentication: Authentication = UsernamePasswordAuthenticationToken(userId, null, authorities)

            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response)
        }
    }

    private fun tokenStealingExceptionResponse(response: HttpServletResponse) {
        response.status = ErrorCode.TOKEN_STEALING.getHttpStatus()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
    }

    private fun deleteTokenAndCookieThrowException(tokenClaim: String, inoValue: String, response: HttpServletResponse) {
        jwtTokenProvider.deleteToken(tokenClaim, inoValue, response)
        tokenStealingExceptionResponse(response)
    }

    private fun deleteTokenCookieThrowException(response: HttpServletResponse) {
        jwtTokenProvider.deleteTokenCookie(response)
        tokenStealingExceptionResponse(response)
    }
}