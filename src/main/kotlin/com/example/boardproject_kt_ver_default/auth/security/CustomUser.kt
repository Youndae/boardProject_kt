package com.example.boardproject_kt_ver_default.auth.security

import com.example.boardproject_kt_ver_default.auth.common.CustomUserDetails
import com.example.boardproject_kt_ver_default.domain.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    username: String,
    password: String,
    authorities: Collection<out GrantedAuthority>,
    val member: Member
): User(username, password, authorities), CustomUserDetails {

    constructor(member: Member): this(
        member.userId,
        member.userPw!!,
        member.auth.map { auth -> SimpleGrantedAuthority(auth.auth) },
        member
    )

    override fun getUserId(): String {
        return member.userId
    }
}