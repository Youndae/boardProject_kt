package com.example.boardproject_kt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token")
class TokenProperties (
    val prefix: String,
    val access: Access,
    val refresh: Refresh
) {
    class Access(
        val header: String,
        val expiration: Long
    )

    class Refresh(
        val header: String,
        val expiration: Long
    )
}
