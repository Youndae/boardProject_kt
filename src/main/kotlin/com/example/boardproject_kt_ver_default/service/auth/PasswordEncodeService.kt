package com.example.boardproject_kt_ver_default.service.auth

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordEncodeService(
    private val passwordEncoder: BCryptPasswordEncoder
) {

    fun encodeMemberPassword(userPw: String): String {

        return passwordEncoder.encode(userPw)
    }
}