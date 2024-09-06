package com.example.boardproject_kt_ver_default.domain.enumuration

enum class Result(val resultMessage: String) {

    SUCCESS("SUCCESS")
    , FAIL("FAIL")
    , ERROR("ERROR")
    , DUPLICATED("DUPLICATED")
    , AVAILABLE("AVAILABLE");

    fun getResultMessage(): String = resultMessage
}