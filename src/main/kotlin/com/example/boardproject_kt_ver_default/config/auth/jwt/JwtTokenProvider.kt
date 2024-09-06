package com.example.boardproject_kt_ver_default.config.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.Date
import java.util.UUID

@Component
class JwtTokenProvider(
    private val redisTemplate: StringRedisTemplate,
    private val jwtProviderProperties: JwtProviderProperties
) {

    fun decodeToken(tokenCookie: Cookie): String {
        var tokenValue = tokenCookie.value.replace(JwtProperties.TOKEN_PREFIX, "")

        return JWT.decode(tokenValue)
                .getClaim("userId")
                .asString()
    }

    fun getTokenValue(token: Cookie): String {
        return token.value.replace(JwtProperties.TOKEN_PREFIX, "")
    }

    fun verifyAccessToken(accessToken: Cookie, inoValue: String): String? {
        val accessTokenValue: String = getTokenValue(accessToken)
        val accessClaimByUserId: String? = getClaimUserIdByToken(accessTokenValue, jwtProviderProperties.accessSecret)

        if(accessClaimByUserId == null)
            return null
        else if(accessClaimByUserId == JwtProperties.TOKEN_EXPIRATION_RESPONSE)
            return JwtProperties.TOKEN_EXPIRATION_RESPONSE
        else if(accessClaimByUserId == JwtProperties.WRONG_TOKEN)
            return JwtProperties.WRONG_TOKEN

        val redisValue: String = getTokenValueData(jwtProviderProperties.redisAccessPrefix, inoValue, accessClaimByUserId)

        if(accessTokenValue == redisValue)
            return accessClaimByUserId
        else {
            deleteTokenData(accessClaimByUserId, inoValue)
            return JwtProperties.TOKEN_STEALING_RESPONSE
        }
    }

    fun verifyRefreshToken(refreshToken: Cookie, inoValue: String, accessTokenClaim: String): String? {
        val refreshTokenValue: String = getTokenValue(refreshToken)
        val refreshClaimByUserId: String? = getClaimUserIdByToken(refreshTokenValue, jwtProviderProperties.refreshSecret)

        if(refreshClaimByUserId == null)
            return null
        else if(refreshClaimByUserId == JwtProperties.TOKEN_EXPIRATION_RESPONSE)
            return JwtProperties.TOKEN_EXPIRATION_RESPONSE
        else if(refreshClaimByUserId == JwtProperties.WRONG_TOKEN)
            return JwtProperties.WRONG_TOKEN
        else if(refreshClaimByUserId != accessTokenClaim){
            deleteTokenData(refreshClaimByUserId, inoValue)
            return JwtProperties.TOKEN_STEALING_RESPONSE
        }

        val redisValue: String = getTokenValueData(jwtProviderProperties.redisRefreshPrefix, inoValue, refreshClaimByUserId)

        if(refreshTokenValue == redisValue)
            return refreshClaimByUserId
        else {
            deleteTokenData(refreshClaimByUserId, inoValue)
            return JwtProperties.TOKEN_STEALING_RESPONSE
        }
    }

    fun getTokenValueData(tokenPrefix: String, inoValue: String, claim: String): String {
        val tokenKey: String = tokenPrefix + inoValue + claim
        val keyExpire: Long = redisTemplate.getExpire(tokenKey)

        if(keyExpire == -2L)
            return "expire"

        return redisTemplate.opsForValue().get(tokenKey) ?: "empty"
    }

    fun getClaimUserIdByToken(tokenValue: String, secret: String): String {
        try{
            return JWT.require(Algorithm.HMAC512(secret))
                        .build()
                        .verify(tokenValue)
                        .getClaim("userId")
                        .asString()
        }catch (e: TokenExpiredException) {
            return JwtProperties.TOKEN_EXPIRATION_RESPONSE
        }catch (e: JWTDecodeException) {
            return JwtProperties.WRONG_TOKEN
        }
    }

    fun issuedToken(userId: String, inoValue: String, response: HttpServletResponse) {
        val accessToken: String = issuedAccessToken(userId, inoValue)
        val refreshToken: String = issuedRefreshToken(userId, inoValue)

        setCookie(JwtProperties.ACCESS_HEADER, accessToken, jwtProviderProperties.tokenCookieAge, response)
        setCookie(JwtProperties.REFRESH_HEADER, refreshToken, jwtProviderProperties.tokenCookieAge, response)
    }

    fun issuedAllToken(userId: String, response: HttpServletResponse) {
        val inoValue: String = issuedIno()
        issuedToken(userId, inoValue, response)

        setCookie(JwtProperties.INO_HEADER, inoValue, jwtProviderProperties.inoCookieAge, response)
    }

    fun issuedAccessToken(userId: String, inoValue: String): String {
        val accessToken: String = createToken(userId, jwtProviderProperties.accessSecret, jwtProviderProperties.accessTokenExpiration)
        setRedisByToken(jwtProviderProperties.redisAccessPrefix, inoValue, userId, accessToken)

        return JwtProperties.TOKEN_PREFIX + accessToken
    }

    fun issuedRefreshToken(userId: String, inoValue: String): String {
        val refreshToken: String = createToken(userId, jwtProviderProperties.refreshSecret, jwtProviderProperties.refreshTokenExpiration)
        setRedisByToken(jwtProviderProperties.redisRefreshPrefix, inoValue, userId, refreshToken)

        return JwtProperties.TOKEN_PREFIX + refreshToken
    }

    fun issuedIno(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    fun createToken(userId: String, secret: String, expirationTime: Long): String {
        return JWT.create()
            .withSubject("cocoToken")
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .withClaim("userId", userId)
            .sign(Algorithm.HMAC512(secret))
    }

    fun setRedisByToken(tokenPrefix: String, ino: String, claim: String, value: String) {
        val key: String = tokenPrefix + ino + claim
        val stringValueOperations: ValueOperations<String, String> = redisTemplate.opsForValue()
        stringValueOperations.set(key, value, Duration.ofDays(jwtProviderProperties.redisExpirationDay))
    }

    fun setCookie(header: String, value: String, expires: Long, response: HttpServletResponse) {
        response.addHeader("Set-Cookie"
            , createCookie(
                header
                , value
                , Duration.ofDays(expires)
            )
        )
    }

    fun createCookie(name: String, value: String, expires: Duration): String {
        return ResponseCookie
            .from(name, value)
            .path("/")
            .maxAge(expires)
            .secure(true)
            .httpOnly(true)
            .sameSite("Strict")
            .build()
            .toString()
    }

    fun deleteToken(userId: String, inoValue: String, response: HttpServletResponse) {
        deleteTokenData(userId, inoValue)
        deleteTokenCookie(response)
    }

    fun deleteTokenData(userId: String, inoValue: String) {
        val accessTokenKey = jwtProviderProperties.redisAccessPrefix + inoValue + userId
        val refreshTokenKey = jwtProviderProperties.redisRefreshPrefix + inoValue + userId

        redisTemplate.delete(accessTokenKey)
        redisTemplate.delete(refreshTokenKey)
    }

    fun deleteTokenCookie(response: HttpServletResponse) {
        val cookieArr = arrayOf(
            JwtProperties.ACCESS_HEADER
            , JwtProperties.REFRESH_HEADER
            , JwtProperties.INO_HEADER
        )

        cookieArr.forEach { name ->
            val cookie = Cookie(name, null).apply {
                maxAge = 0
                path = "/"
            }
            response.addCookie(cookie)
        }
    }

    fun printTest() {
        println(jwtProviderProperties.accessSecret)
        println(jwtProviderProperties.redisAccessPrefix)
    }
}