package com.example.boardproject_kt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cookie")
class CookieProperties(
    val ino: Ino
) {
    class Ino(
        val header: String,
        val age: Int
    )
}
