package com.example.boardproject_kt.auth.oAuth

import com.example.boardproject_kt.auth.oAuth.domain.OAuth2Member
import com.example.boardproject_kt.auth.user.CustomUserDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val oAuth2Member: OAuth2Member
): OAuth2User, CustomUserDetails {

    override fun getAttributes(): Map<String, Any> = emptyMap()

    override fun getName(): String? = oAuth2Member.username

    override fun getAuthorities(): Collection<GrantedAuthority> = oAuth2Member.authList
        .map { v -> SimpleGrantedAuthority(v.auth) }

    override val userId: String
        get() = oAuth2Member.userId

    /*override val authorities: Collection<GrantedAuthority>
        get() = getAuthorities()*/

    fun getNickname(): String? = oAuth2Member.nickname
}