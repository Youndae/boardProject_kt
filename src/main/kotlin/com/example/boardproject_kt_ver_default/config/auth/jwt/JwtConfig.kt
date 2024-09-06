package com.example.boardproject_kt_ver_default.config.auth.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class JwtConfig {

    @Bean
    fun jwtProperties(
        @Value("#{jwt['token.access.secret']}") accessSecret: String,
        @Value("#{jwt['token.access.expiration']}") accessTokenExpiration: Long,
        @Value("#{jwt['token.refresh.secret']}") refreshSecret: String,
        @Value("#{jwt['token.refresh.expiration']}") refreshTokenExpiration: Long,
        @Value("#{jwt['redis.expirationDay']}") redisExpirationDay: Long,
        @Value("#{jwt['redis.accessPrefix']}") redisAccessPrefix: String,
        @Value("#{jwt['redis.refreshPrefix']}") redisRefreshPrefix: String,
        @Value("#{jwt['cookie.tokenAgeDay']}") tokenCookieAge: Long,
        @Value("#{jwt['cookie.inoAgeDay']}") inoCookieAge: Long
    ): JwtProviderProperties {
        return JwtProviderProperties(
            accessSecret
            , accessTokenExpiration
            , refreshSecret
            , refreshTokenExpiration
            , redisExpirationDay
            , redisAccessPrefix
            , redisRefreshPrefix
            , tokenCookieAge
            , inoCookieAge
        )
    }

    @Bean
    fun jwtTokenProvider(
        redisTemplate: StringRedisTemplate,
        jwtProviderProperties: JwtProviderProperties
    ): JwtTokenProvider {
        return JwtTokenProvider(redisTemplate, jwtProviderProperties)
    }
}