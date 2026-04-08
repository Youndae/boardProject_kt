package com.example.boardproject_kt.auth.oAuth.response

import com.example.boardproject_kt.domain.enumuration.OAuthProvider

class NaverResponse(
    private val attributes: Map<String, Any>
): OAuth2Response {

    private val responseAttributes = attributes["response"] as? Map<String, Any> ?: emptyMap()

    override val provider: String = OAuthProvider.NAVER.key

    override val providerId: String
        get() = responseAttributes["id"].toString()

    override val email: String
        get() = responseAttributes["email"].toString()

    override val name: String
        get() = responseAttributes["name"].toString()
}