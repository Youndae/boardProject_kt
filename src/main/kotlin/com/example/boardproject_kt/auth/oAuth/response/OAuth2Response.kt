package com.example.boardproject_kt.auth.oAuth.response

interface OAuth2Response {
    val provider: String
    val providerId: String
    val email: String
    val name: String
}