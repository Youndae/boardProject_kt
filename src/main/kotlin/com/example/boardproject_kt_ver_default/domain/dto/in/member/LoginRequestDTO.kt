package com.example.boardproject_kt_ver_default.domain.dto.`in`.member

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequestDTO @JsonCreator constructor(
    @JsonProperty("userId") val userId: String,
    @JsonProperty("userPw") var userPw: String
)