package com.example.boardproject_kt_ver_default.auth.oAuth.domain

import com.example.boardproject_kt_ver_default.domain.enumuration.OAuthProvider

class KakaoResponse(
    private val attribute: Map<String, Any>
): OAuth2Response {

    private val accountAttribute: Map<String, Any> = attribute["kakao_account"] as Map<String, Any>
    private val profileAttribute: Map<String, Any> = accountAttribute["profile"] as Map<String, Any>

    override fun getProvider(): String {
        return OAuthProvider.KAKAO.key
    }

    override fun getProviderId(): String {
        return attribute["id"].toString()
    }

    override fun getEmail(): String {
        return accountAttribute["email"].toString()
    }

    override fun getName(): String {
        return profileAttribute["nickname"].toString()
    }
}