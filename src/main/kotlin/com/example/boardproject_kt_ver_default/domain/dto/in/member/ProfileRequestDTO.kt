package com.example.boardproject_kt_ver_default.domain.dto.`in`.member

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

open class ProfileRequestDTO @JsonCreator constructor(
    @JsonProperty("nickname") open val nickname: String
) {
}