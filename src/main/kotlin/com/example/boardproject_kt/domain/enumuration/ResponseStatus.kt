package com.example.boardproject_kt.domain.enumuration

enum class ResponseStatus(
    val message: String
) {

    SUCCESS("success"),
    FAIL("fail"),
    ERROR("error");
}