package com.example.boardproject_kt.domain.dto.member.request

import org.springframework.web.multipart.MultipartFile

data class JoinRequest(
    val userId: String,
    val password: String,
    val userName: String,
    val nickname: String,
    val email: String,
    val profile: MultipartFile? = null
) {
    fun hasProfile() = profile != null && !profile.isEmpty
}
