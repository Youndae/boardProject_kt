package com.example.boardproject_kt.domain.dto.member.request

import org.springframework.web.multipart.MultipartFile

data class UpdateProfileRequest(
    val nickname: String,
    val email: String,
    val profile: MultipartFile? = null,
    val deleteProfile: String? = null
) {
    fun hasProfile() = profile != null && !profile.isEmpty

    fun hasDeleteProfile() = deleteProfile != null
}
