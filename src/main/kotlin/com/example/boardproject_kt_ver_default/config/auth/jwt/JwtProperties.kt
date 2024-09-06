package com.example.boardproject_kt_ver_default.config.auth.jwt

object JwtProperties {

    const val TOKEN_PREFIX = "Bearer"
    const val ACCESS_HEADER = "Authorization"
    const val REFRESH_HEADER = "Authorization_Refresh"
    const val INO_HEADER = "Authorization_ino"
    const val TOKEN_STEALING_RESPONSE = "st"
    const val TOKEN_EXPIRATION_RESPONSE = "expiration_token"
    const val WRONG_TOKEN = "wrong_token"
}