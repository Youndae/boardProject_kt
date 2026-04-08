package com.example.boardproject_kt.domain.enumuration

enum class MemberCheckResult(
    val message: String
) {
    VALID("VALID"),
    INVALID("INVALID"),
    DUPLICATED("DUPLICATED");
}