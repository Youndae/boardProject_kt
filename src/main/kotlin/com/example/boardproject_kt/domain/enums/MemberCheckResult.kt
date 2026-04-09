package com.example.boardproject_kt.domain.enums

enum class MemberCheckResult(
    val message: String
) {
    VALID("VALID"),
    INVALID("INVALID"),
    DUPLICATED("DUPLICATED");
}