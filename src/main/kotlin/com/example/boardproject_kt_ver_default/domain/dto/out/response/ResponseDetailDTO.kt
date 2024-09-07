package com.example.boardproject_kt_ver_default.domain.dto.out.response

data class ResponseDetailDTO<T>(
    val content: T,
    val userStats: UserStatusDTO
) {
    constructor(responseContent: T, nickname: String?): this(
        content = responseContent,
        userStats = UserStatusDTO(nickname)
    )
}
