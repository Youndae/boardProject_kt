package com.example.boardproject_kt_ver_default.domain.dto.out.member

import java.security.Principal

data class LoginStatusDTO(
    val loginStatus: Boolean
) {
    constructor(principal: Principal?): this(principal != null)
}
