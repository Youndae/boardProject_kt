package com.example.boardproject_kt.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.boardproject_kt.domain.enums.TokenValidationResult
import com.example.boardproject_kt.properties.CookieProperties
import com.example.boardproject_kt.properties.JwtSecretProperties
import com.example.boardproject_kt.properties.TokenProperties
import com.example.boardproject_kt.properties.TokenRedisProperties
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.Date
import java.util.UUID

@Component
class TokenProvider(
    private val tokenProperties: TokenProperties,
    private val tokenRedisProperties: TokenRedisProperties,
    private val cookieProperties: CookieProperties,
    private val jwtSecretProperties: JwtSecretProperties,
    private val redisTemplate: StringRedisTemplate,
) {

    fun decodeToken(tokenCookie: Cookie): String {
        val tokenValue = getTokenCookieValue(tokenCookie)

        return JWT.decode(tokenValue)
            .getClaim("userId")
            .asString()
    }

    fun verifyAccessToken(accessToken: Cookie, inoValue: String): String {
        val accessTokenValue = getTokenCookieValue(accessToken)
        val accessClaimByUserId = getClaimUserIdByToken(accessTokenValue, jwtSecretProperties.access)

        when(accessClaimByUserId) {
            TokenValidationResult.WRONG_TOKEN.result -> return TokenValidationResult.WRONG_TOKEN.result
            TokenValidationResult.TOKEN_EXPIRATION.result -> return TokenValidationResult.TOKEN_EXPIRATION.result
        }

        val redisValue: String? = getTokenValueData(tokenRedisProperties.access.prefix, inoValue, accessClaimByUserId)

        return if(accessTokenValue == redisValue)
            accessClaimByUserId
        else{
            deleteTokenData(accessClaimByUserId, inoValue)
            TokenValidationResult.TOKEN_STEALING.result
        }
    }

    fun verifyRefreshToken(refreshToken: Cookie, inoValue: String, accessTokenClaim: String): String {
        val refreshTokenValue = getTokenCookieValue(refreshToken)
        val refreshClaimByUserId = getClaimUserIdByToken(refreshTokenValue, jwtSecretProperties.refresh)

        when(refreshClaimByUserId) {
            TokenValidationResult.WRONG_TOKEN.result -> TokenValidationResult.WRONG_TOKEN.result
            TokenValidationResult.TOKEN_EXPIRATION.result -> TokenValidationResult.TOKEN_EXPIRATION.result
        }

        if(refreshClaimByUserId != accessTokenClaim){
            deleteTokenData(refreshClaimByUserId, inoValue)
            return TokenValidationResult.TOKEN_STEALING.result
        }

        val redisValue: String? = getTokenValueData(tokenRedisProperties.refresh.prefix, inoValue, refreshClaimByUserId)

        return if(refreshTokenValue == redisValue)
            refreshClaimByUserId
        else {
            deleteTokenData(refreshClaimByUserId, inoValue)
            TokenValidationResult.TOKEN_STEALING.result
        }
    }

    private fun getTokenCookieValue(token: Cookie): String =
         token.value.replace(tokenProperties.prefix, "")


    private fun getTokenValueData(tokenPrefix: String, inoValue: String, claim: String): String? {
        val tokenKey = createRedisKey(tokenPrefix, inoValue, claim)
        val keyExpire = redisTemplate.getExpire(tokenKey)

        if(keyExpire == -2L)
            return null

        return redisTemplate.opsForValue().get(tokenKey)
    }

    private fun getClaimUserIdByToken(tokenValue: String, secret: String): String {
        return try {
            val claim = JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(tokenValue)
                .getClaim("userId")
                .asString()

            claim ?: TokenValidationResult.WRONG_TOKEN.result
        }catch (e: TokenExpiredException) {
            TokenValidationResult.TOKEN_EXPIRATION.result
        }catch (e: JWTDecodeException) {
            TokenValidationResult.WRONG_TOKEN.result
        }
    }

    fun issuedToken(userId: String, inoValue: String, response: HttpServletResponse) {
        val accessToken = issuedAccessToken(userId, inoValue)
        val refreshToken = issuedRefreshToken(userId, inoValue)
        val accessExpiration = getAccessTokenRedisAndCookieDuration()
        val refreshExpiration = getRefreshTokenRedisAndCookieDuration()

        setCookie(tokenProperties.access.header, accessToken, accessExpiration, response)
        setCookie(tokenProperties.refresh.header, refreshToken, refreshExpiration, response)
    }

    fun issuedAllToken(userId: String, response: HttpServletResponse) {
        val inoValue = issuedIno()
        issuedToken(userId, inoValue, response)

        setCookie(
            cookieProperties.ino.header,
            inoValue,
            Duration.ofDays(cookieProperties.ino.age.toLong()),
            response
        )
    }

    private fun issuedAccessToken(userId: String, inoValue: String): String {
        val accessToken = createToken(userId, jwtSecretProperties.access, tokenProperties.access.expiration)
        val accessExpiration = getAccessTokenRedisAndCookieDuration()
        setRedisByToken(
            tokenRedisProperties.access.prefix,
            inoValue,
            userId,
            accessToken,
            accessExpiration
        )

        return "${tokenProperties.prefix}$accessToken"
    }

    private fun issuedRefreshToken(userId: String, inoValue: String): String {
        val refreshToken = createToken(userId, jwtSecretProperties.refresh, tokenProperties.refresh.expiration)
        val refreshExpiration = getRefreshTokenRedisAndCookieDuration()
        setRedisByToken(
            tokenRedisProperties.refresh.prefix,
            inoValue,
            userId,
            refreshToken,
            refreshExpiration
        )

        return "${tokenProperties.prefix}$refreshToken"
    }

    private fun getAccessTokenRedisAndCookieDuration(): Duration =
        Duration.ofHours(tokenRedisProperties.access.expiration.toLong())

    private fun getRefreshTokenRedisAndCookieDuration(): Duration =
        Duration.ofDays(tokenRedisProperties.refresh.expiration.toLong())

    private fun issuedIno(): String = UUID.randomUUID().toString().replace("-", "")

    private fun createToken(userId: String, secret: String, expirationTime: Long): String =
        JWT.create()
            .withSubject("cocoToken")
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .withClaim("userId", userId)
            .sign(Algorithm.HMAC512(secret))

    private fun setRedisByToken(
        tokenPrefix: String,
        ino: String,
        claim: String,
        value: String,
        expiration: Duration
    ) {
        val key = createRedisKey(tokenPrefix, ino, claim)

        redisTemplate.opsForValue().set(key, value, expiration)
    }

    private fun setCookie(
        header: String,
        value: String,
        expires: Duration,
        response: HttpServletResponse
    ) {
        response.addHeader(
            "Set-Cookie",
            createCookie(
                header, value, expires
            )
        )
    }

    private fun createCookie(name: String, value: String, expires: Duration): String =
        ResponseCookie
            .from(name, value)
            .path("/")
            .maxAge(expires)
            .secure(true)
            .httpOnly(true)
            .sameSite("Strict")
            .build()
            .toString()

    fun deleteToken(userId: String, inoValue: String, response: HttpServletResponse) {
        deleteTokenData(userId, inoValue)
        deleteTokenCookie(response)
    }

    fun deleteTokenData(userId: String, inoValue: String) {
        val keys = listOf(
            createRedisKey(
                tokenRedisProperties.access.prefix,
                inoValue,
                userId
            ),
            createRedisKey(
                tokenRedisProperties.refresh.prefix,
                inoValue,
                userId
            )
        )

        redisTemplate.delete(keys)
    }

    fun deleteTokenCookie(response: HttpServletResponse) {
        val cookieNames = arrayOf(
            tokenProperties.access.header,
            tokenProperties.refresh.header,
            cookieProperties.ino.header
        )

        cookieNames.forEach { name ->
            val cookie = Cookie(name, null).apply {
                maxAge = 0
                path = "/"
                isHttpOnly = true
            }
            response.addCookie(cookie)
        }

    }

    private fun createRedisKey(prefix: String, ino: String, userId: String): String = "$prefix$ino$userId"
}