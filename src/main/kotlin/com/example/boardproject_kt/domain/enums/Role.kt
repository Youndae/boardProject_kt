package com.example.boardproject_kt.domain.enums

enum class Role(
    val key: String
) {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    companion object {
        fun of(key: String): Role {
            return entries.find{ it.key == key } ?: MEMBER
        }
    }
}