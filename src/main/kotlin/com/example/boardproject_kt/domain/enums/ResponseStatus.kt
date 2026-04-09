package com.example.boardproject_kt.domain.enums

enum class ResponseStatus(
    val message: String
) {

    SUCCESS("success"),
    FAIL("fail"),
    ERROR("error");
}