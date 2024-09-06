package com.example.boardproject_kt_ver_default.auth.oAuth.domain

import com.example.boardproject_kt_ver_default.domain.enumuration.OAuthProvider

class GoogleResponse(
    private val attribute: Map<String, Any>
): OAuth2Response {

    override fun getProvider(): String {
        return OAuthProvider.GOOGLE.getKey()
    }

    override fun getProviderId(): String {
        return attribute["sub"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }

    override fun getName(): String {
        return attribute["name"].toString()
    }
}