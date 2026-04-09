package com.example.boardproject_kt.auth.oAuth.response

import com.example.boardproject_kt.domain.enums.OAuthProvider

class GoogleResponse(
    private val attributes: Map<String, Any>
) : OAuth2Response {

    override val provider: String = OAuthProvider.GOOGLE.key

    override val providerId: String
        get() = attributes["sub"].toString()

    override val email: String
        get() = attributes["email"].toString()

    override val name: String
        get() = attributes["name"].toString()
}