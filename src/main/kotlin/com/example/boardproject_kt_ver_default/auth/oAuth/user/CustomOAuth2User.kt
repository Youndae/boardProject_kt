package com.example.boardproject_kt_ver_default.auth.oAuth.user

import com.example.boardproject_kt_ver_default.auth.common.CustomUserDetails
import com.example.boardproject_kt_ver_default.auth.oAuth.domain.OAuth2DTO
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val oAuth2DTO: OAuth2DTO
): OAuth2User, CustomUserDetails {

    override fun getName(): String {
        return oAuth2DTO.username
    }

    override fun getAttributes(): MutableMap<String, Any>? {
        return null
    }

    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return oAuth2DTO.authList
            .map { auth -> SimpleGrantedAuthority(auth.auth) }
            .toList()
    }

    override fun getUserId(): String {
        return oAuth2DTO.userId
    }

    fun getNickname(): String? {
        return oAuth2DTO.nickname
    }
}