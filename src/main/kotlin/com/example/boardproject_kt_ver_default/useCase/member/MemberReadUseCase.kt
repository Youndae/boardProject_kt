package com.example.boardproject_kt_ver_default.useCase.member

import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.LoginRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.member.ProfileResponseDTO
import com.example.boardproject_kt_ver_default.service.auth.PasswordEncodeService
import com.example.boardproject_kt_ver_default.service.member.MemberReadService
import com.example.boardproject_kt_ver_default.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class MemberReadUseCase(
    private val memberReadService: MemberReadService,
    private val passwordEncodeService: PasswordEncodeService
) {

    private val log = logger()

    fun checkUserId(userId: String): String {

        return memberReadService.checkId(userId)
    }

    fun checkNickname(nickname: String, principal: Principal): String {

        return memberReadService.checkNickname(nickname, principal)
    }

    fun getProfile(principal: Principal): ProfileResponseDTO {

        return memberReadService.getProfile(principal)
    }

    fun login(member: LoginRequestDTO
              , request: HttpServletRequest
              , response: HttpServletResponse): String {

        member.userPw = passwordEncodeService.encodeMemberPassword(member.userPw)

        return memberReadService.loginProc(member, request, response)
    }

    fun logout(principal: Principal
               , request: HttpServletRequest
               , response: HttpServletResponse): String {

        return memberReadService.logoutProc(principal, request, response)
    }
}