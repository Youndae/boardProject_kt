package com.example.boardproject_kt_ver_default.domain.dto.business.auth

import com.example.boardproject_kt_ver_default.domain.entity.Member

data class PrincipalDTO(
    val userId: String,
    val nickname: String,
    val provider: String
) {

    fun toMemberEntity(): Member {
        return Member(
            userId = this.userId,
            username = "",
            email = "",
            nickName = this.nickname,
            provider = this.provider
        )
    }

    constructor(member: Member): this(
        member.userId,
        member.nickName,
        member.provider
    )
}
