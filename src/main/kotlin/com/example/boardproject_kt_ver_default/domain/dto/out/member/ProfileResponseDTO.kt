package com.example.boardproject_kt_ver_default.domain.dto.out.member

import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.ProfileRequestDTO

class ProfileResponseDTO(
    override val nickname: String,
    val profileThumbnail: String?
): ProfileRequestDTO(nickname) {
}