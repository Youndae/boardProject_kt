package com.example.boardproject_kt.auth.oAuth.response

import com.example.boardproject_kt.domain.enums.OAuthProvider

class KakaoResponse(private val attributes: Map<String, Any>): OAuth2Response {

    private val accountAttributes = attributes["kakao_account"] as Map<String, Any>
    private val profileAttributes = accountAttributes["profile"] as Map<String, Any>

    override val provider: String = OAuthProvider.KAKAO.key

    override val providerId: String
        get() = attributes["id"].toString()

    override val email: String
        get() = accountAttributes["email"].toString()

    override val name: String
        get() = profileAttributes["nickname"].toString()
}