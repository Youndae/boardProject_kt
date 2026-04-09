package com.example.boardproject_kt.domain.enums

enum class OAuthProvider(
    val key: String
) {
    LOCAL("local"),
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");
}