package com.example.boardproject_kt_ver_default.service.auth

import com.example.boardproject_kt_ver_default.domain.dto.business.auth.PrincipalDTO
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import com.example.boardproject_kt_ver_default.repository.member.MemberRepository
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class PrincipalReadService(
    private val memberRepository: MemberRepository
) {
    fun getNicknameToPrincipal(principal: Principal?): String? {
        if(principal != null){
            val member: Member = getMemberEntity(principal)

            return member.nickName
        }

        return null
    }

    fun checkPrincipal(principal: Principal): PrincipalDTO {
        try {
            val member: Member = getMemberEntity(principal)

            return PrincipalDTO(member)
        }catch (e: Exception){
            throw CustomAccessDeniedException("checkPrincipal Exception : ${e.message}")
        }
    }

    fun getMemberEntity(principal: Principal): Member {
        return memberRepository.findByUserId(principal.name)
    }
}