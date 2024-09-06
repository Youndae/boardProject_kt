package com.example.boardproject_kt_ver_default.domain.dto.out.response

import org.springframework.data.domain.Page

data class ResponsePageableListDTO<T>(
    val content: List<T>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Long,
    val totalPages: Long,
    val userStatus: UserStatusDTO
) {
    constructor(pageableResponse: Page<T>, nickname: String?): this(
        content = pageableResponse.content,
        empty = pageableResponse.isEmpty,
        first = pageableResponse.isFirst,
        last = pageableResponse.isLast,
        number = pageableResponse.number.toLong(),
        totalPages = pageableResponse.totalPages.toLong(),
        userStatus = UserStatusDTO(nickname)
    )
}
