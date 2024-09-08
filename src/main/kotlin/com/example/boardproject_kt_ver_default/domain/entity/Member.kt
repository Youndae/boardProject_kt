package com.example.boardproject_kt_ver_default.domain.entity

import com.example.boardproject_kt_ver_default.domain.enumuration.Role
import jakarta.persistence.*
import lombok.NonNull

@Entity
class Member(
    @Id var userId: String,
    var username: String,
    var email: String,
    @Column(unique = true) var nickName: String,
    @NonNull var provider: String
) {

    var userPw: String? = null
    var profileThumbnail: String? = null

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var auths: MutableList<Auth> = mutableListOf()
    /*val auth: List<Auth>
        get() = auths.toList()*/

    constructor(
        userId: String,
        userPw: String,
        username: String,
        email: String,
        nickname: String,
        profileThumbnail: String?,
        provider: String
    ): this(
        userId,
        username,
        email,
        nickname,
        provider
    ) {
        this.userPw = userPw
        this.profileThumbnail = profileThumbnail
    }

    fun addAuth() {
        val auth = Auth(Role.MEMBER.key)
        auths += auth
        auth.memberSet(this)
    }

}