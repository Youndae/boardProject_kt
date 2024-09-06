package com.example.boardproject_kt_ver_default.domain.enumuration

enum class OAuthProvider(val key: String) {

    LOCAL("local"),
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    fun getKey(): String = key
}