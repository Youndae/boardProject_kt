package com.example.boardproject_kt_ver_default.auth.oAuth.domain

import com.example.boardproject_kt_ver_default.domain.entity.Auth
import com.example.boardproject_kt_ver_default.domain.entity.Member

data class OAuth2DTO(
    val userId: String,
    val username: String,
    val authList: List<Auth>,
    val nickname: String?
) {
    constructor(existsData: Member): this(
        existsData.userId,
        existsData.username,
        existsData.auth,
        existsData.nickName
    )
}
