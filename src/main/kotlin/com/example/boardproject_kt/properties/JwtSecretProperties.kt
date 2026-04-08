package com.example.boardproject_kt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt.secret")
class JwtSecretProperties(
    val access: String,
    val refresh: String
)
