package com.example.boardproject_kt_ver_default.auth.oAuth.converter

import com.example.boardproject_kt_ver_default.auth.oAuth.domain.OAuth2Response
import com.example.boardproject_kt_ver_default.domain.entity.Member

class OAuth2ResponseEntityConverter {
    fun toEntity(oAuth2Response: OAuth2Response, userId: String): Member {
        return Member(
            userId = userId,
            email = oAuth2Response.getEmail(),
            username = oAuth2Response.getName(),
            provider = oAuth2Response.getProvider(),
            nickName = ""
        )
    }
}