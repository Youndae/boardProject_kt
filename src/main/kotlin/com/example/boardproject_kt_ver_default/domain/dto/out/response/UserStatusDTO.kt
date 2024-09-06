package com.example.boardproject_kt_ver_default.domain.dto.out.response

data class UserStatusDTO(
    val loggedIn: Boolean,
    val uid: String?
) {
    constructor(nickname: String?) : this(
        loggedIn = nickname  != null,
        uid = nickname
    )
}
