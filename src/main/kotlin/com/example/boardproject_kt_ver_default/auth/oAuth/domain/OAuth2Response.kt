package com.example.boardproject_kt_ver_default.auth.oAuth.domain

interface OAuth2Response {

    fun getProvider(): String

    fun getProviderId(): String

    fun getEmail(): String

    fun getName(): String
}