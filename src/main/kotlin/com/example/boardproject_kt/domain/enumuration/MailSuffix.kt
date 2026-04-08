package com.example.boardproject_kt.domain.enumuration

enum class MailSuffix(
    val mailSuffixType: String
) {
    NAVER("naver"),
    DAUM("daum"),
    GMAIL("gmail"),
    NONE("none");

    companion object {
        fun findSuffixType(suffix: String): String {
            return entries.find{ it.mailSuffixType == suffix }?.mailSuffixType
                ?: NONE.mailSuffixType
        }
    }
}