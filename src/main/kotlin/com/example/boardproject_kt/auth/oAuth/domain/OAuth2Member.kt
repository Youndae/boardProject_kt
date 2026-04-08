package com.example.boardproject_kt.auth.oAuth.domain

import com.example.boardproject_kt.domain.entity.Auth
import com.example.boardproject_kt.domain.entity.Member

data class OAuth2Member(
    val userId: String,
    val username: String,
    val authList: List<Auth>,
    val nickname: String? = null
) {
    constructor(existsData: Member): this(
        existsData.userId,
        existsData.username,
        existsData.auths,
        existsData.nickname
    )
}
