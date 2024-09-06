package com.example.boardproject_kt_ver_default.config.auth.jwt

data class JwtProviderProperties(
    val accessSecret: String,
    val accessTokenExpiration: Long,
    val refreshSecret: String,
    val refreshTokenExpiration: Long,
    val redisExpirationDay: Long,
    val redisAccessPrefix: String,
    val redisRefreshPrefix: String,
    val tokenCookieAge: Long,
    val inoCookieAge: Long
)
