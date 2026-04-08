package com.example.boardproject_kt.domain.enumuration

enum class TokenValidationResult(
    val result: String
) {

    TOKEN_STEALING("TOKEN_STEALING"),
    TOKEN_EXPIRATION("TOKEN_EXPIRATION"),
    WRONG_TOKEN("INVALID_TOKEN");
}