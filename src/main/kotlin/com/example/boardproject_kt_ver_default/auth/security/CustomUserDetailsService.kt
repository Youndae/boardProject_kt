package com.example.boardproject_kt_ver_default.auth.security

import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.repository.member.MemberRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class CustomUserDetailsService(
    private val repository: MemberRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val member: Member? = repository.findByLocalUserId(username!!)

        if(member == null)
            throw BadCredentialsException("loadUser fail")
        else
            return CustomUser(member)
    }
}