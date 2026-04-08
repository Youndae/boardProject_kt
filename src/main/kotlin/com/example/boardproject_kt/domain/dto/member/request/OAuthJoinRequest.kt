package com.example.boardproject_kt.domain.dto.member.request

import org.springframework.web.multipart.MultipartFile

data class OAuthJoinRequest(
    val nickname: String,
    val profile: MultipartFile? = null
) {
    fun hasProfile() = profile != null && !profile.isEmpty
}
