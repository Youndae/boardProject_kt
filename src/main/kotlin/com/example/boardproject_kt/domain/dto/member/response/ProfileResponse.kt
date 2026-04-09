package com.example.boardproject_kt.domain.dto.member.response

import com.example.boardproject_kt.domain.enums.MailSuffix

data class ProfileResponse(
    var nickname: String? = null,
    var mailPrefix: String? = null,
    var mailSuffix: String? = null,
    var mailType: String? = null,
    var profile: String? = null
) {
    constructor(
        nickname: String,
        email: String,
        profile: String?
    ): this() {
        val splitMail = email.split("@")
        if(splitMail.size == 2) {
            val suffixContent = splitMail[1].substringBefore(".")
            val type = MailSuffix.findSuffixType(suffixContent)

            this.nickname = nickname
            this.mailPrefix = splitMail[0]
            this.mailSuffix = splitMail[1]
            this.mailType = type
            this.profile = profile
        }
    }
}
