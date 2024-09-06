package com.example.boardproject_kt_ver_default.auth.common

import org.springframework.security.core.GrantedAuthority

interface CustomUserDetails {

    fun getUserId(): String

    fun getAuthorities(): Collection<out GrantedAuthority>
}