package com.example.boardproject_kt_ver_default.domain.enumuration

enum class Role(private val key: String) {

    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    fun getKey(): String = key
}