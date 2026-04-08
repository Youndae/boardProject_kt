package com.example.boardproject_kt.auth.user

import com.example.boardproject_kt.domain.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser : User, CustomUserDetails {

    val member: Member

    constructor(
        username: String,
        password: String,
        authorities: Collection<GrantedAuthority>,
        member: Member
    ) : super(username, password, authorities) {
        this.member = member
    }

    constructor(member: Member): super(
            member.userId,
            member.password,
            member.auths.map { SimpleGrantedAuthority(it.auth) }
    ) {
        this.member = member
    }

    override val userId: String
        get() = member.userId

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return super.getAuthorities()
    }
}