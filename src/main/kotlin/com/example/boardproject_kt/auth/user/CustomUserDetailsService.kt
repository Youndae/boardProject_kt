package com.example.boardproject_kt.auth.user

import com.example.boardproject_kt.domain.entity.Member
import com.example.boardproject_kt.repository.MemberRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val member: Member = memberRepository.findByLoginUserId(username)
            ?: throw BadCredentialsException("loadUser fail")

        return CustomUser(member)
    }
}