package com.example.boardproject_kt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token-redis")
class TokenRedisProperties(
    val access: Access,
    val refresh: Refresh
) {
    class Access(
        val expiration: Int,
        val prefix: String
    )

    class Refresh(
        val expiration: Int,
        val prefix: String
    )
}
