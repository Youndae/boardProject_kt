package com.example.boardproject_kt_ver_default.domain.dto.`in`.member

import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.domain.enumuration.OAuthProvider
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

data class JoinRequestDTO @JsonCreator constructor(
    @JsonProperty("userId") val userId: String,
    @JsonProperty("userPw") var userPw: String,
    @JsonProperty("userName") val userName: String,
    @JsonProperty("nickname") val nickname: String,
    @JsonProperty("email") val email: String
) {
    fun toEntity(profile: String?): Member {

        return Member(
            this.userId,
            this.userPw,
            this.userName,
            this.email,
            this.nickname,
            profile,
            OAuthProvider.LOCAL.key
        )
    }
}
