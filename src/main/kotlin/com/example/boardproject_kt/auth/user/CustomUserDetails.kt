package com.example.boardproject_kt.auth.user

import org.springframework.security.core.GrantedAuthority

interface CustomUserDetails {
    val userId: String
//    val authorities: Collection<GrantedAuthority>
    fun getAuthorities(): Collection<GrantedAuthority>
}
