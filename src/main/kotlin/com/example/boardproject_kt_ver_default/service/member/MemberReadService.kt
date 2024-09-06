package com.example.boardproject_kt_ver_default.service.member

import com.example.boardproject_kt_ver_default.auth.security.CustomUser
import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtProperties
import com.example.boardproject_kt_ver_default.config.auth.jwt.JwtTokenProvider
import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.LoginRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.member.ProfileResponseDTO
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.repository.member.MemberRepository
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.stereotype.Service
import com.example.boardproject_kt_ver_default.domain.enumuration.Result
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.web.util.WebUtils
import java.security.Principal

@Service
class MemberReadService(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: JwtTokenProvider
) {

    private val log = logger()

    fun checkId(userId: String): String {
        val member: Member? = memberRepository.findByLocalUserId(userId)

        return if (member == null)
                    Result.AVAILABLE.getResultMessage()
                else
                    Result.DUPLICATED.getResultMessage()
    }

    fun checkNickname(nickname: String, principal: Principal?): String {
        val member: Member? = memberRepository.findByNickname(nickname)

        return if(member == null){
                    Result.AVAILABLE.getResultMessage()
                }else {
                    if(principal != null && member.userId == principal.name){
                        Result.AVAILABLE.getResultMessage()
                    }else {
                        Result.DUPLICATED.getResultMessage()
                    }
                }
    }

    fun getProfile(principal: Principal): ProfileResponseDTO {
        val member: Member = memberRepository.findByUserId(principal.name)

        return ProfileResponseDTO(member.nickName, member.profileThumbnail)
    }

    fun loginProc(member: LoginRequestDTO
                , request: HttpServletRequest
                , response: HttpServletResponse): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(member.userId, member.userPw)
        val authentication: Authentication = authenticationManagerBuilder
                                                .`object`
                                                .authenticate(authenticationToken)

        val inoCookie: Cookie? = WebUtils.getCookie(request, JwtProperties.INO_HEADER)

        if(inoCookie == null)
            tokenProvider.issuedAllToken(member.userId, response)
        else
            tokenProvider.issuedToken(member.userId, inoCookie.value, response)

        return Result.SUCCESS.resultMessage
    }

    fun logoutProc(principal: Principal
                , request: HttpServletRequest
                , response: HttpServletResponse): String {

        val inoValue: String = WebUtils.getCookie(request, JwtProperties.INO_HEADER)!!.value
        val userId: String = principal.name

        return try {
                    tokenProvider.deleteToken(userId, inoValue, response)

                    Result.SUCCESS.resultMessage
                }catch (e: Exception) {
                    log.error("logout Exception : ${e.message}")

                    Result.FAIL.resultMessage
                }
    }
}