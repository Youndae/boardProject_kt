package com.example.boardproject_kt.auth.oAuth.converter

import com.example.boardproject_kt.auth.oAuth.response.OAuth2Response
import com.example.boardproject_kt.domain.entity.Member

class OAuth2ResponseEntityConverter {

    companion object {
        fun toEntity(
            oAuth2Response: OAuth2Response,
            userId: String
        ): Member = Member(
            userId = userId,
            email = oAuth2Response.email,
            username = oAuth2Response.name,
            provider = oAuth2Response.provider
        )
    }
}