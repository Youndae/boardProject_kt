package com.example.boardproject_kt.service.member

import com.example.boardproject_kt.auth.user.CustomUser
import com.example.boardproject_kt.domain.dto.member.response.MemberStatusResponse
import com.example.boardproject_kt.domain.enumuration.Role
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthContextService {

    fun getMemberStatus(authentication: Authentication): MemberStatusResponse {
        val userId = when (val principal = authentication.principal) {
            is CustomUser -> principal.userId
            is String -> principal
            else -> principal.toString()
        }

        val roleKey = authentication.authorities
            .map { auth -> Role.of(auth.authority) }
            .maxByOrNull { it.ordinal }
            ?.key ?: Role.MEMBER.key

        return MemberStatusResponse(
            userId = userId,
            role = roleKey
        )
    }
}