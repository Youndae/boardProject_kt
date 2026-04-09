package com.example.boardproject_kt.domain.entity

import com.example.boardproject_kt.auth.oAuth.domain.OAuth2Member
import com.example.boardproject_kt.domain.enums.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", length = 100, unique = true, nullable = false)
    val userId: String,

    @Column(nullable = true)
    var password: String? = null,

    @Column(name = "user_name", nullable = false, length = 50)
    var username: String,

    @Column(nullable = false, length = 100)
    var email: String,

    @Column(unique = true, length = 50)
    var nickname: String? = null,

    var profile: String? = null,

    @Column(nullable = false)
    val provider: String,

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val auths: MutableList<Auth> = mutableListOf()
) {

    fun addAuth() {
        val auth = Auth(
            auth = Role.MEMBER.key,
            member = this
        )
        this.auths.add(auth)
    }

    fun updatePassword(rawPassword: String, passwordEncoder: BCryptPasswordEncoder) {
        this.password = passwordEncoder.encode(rawPassword)
    }

    fun updateProfile(profile: String) {
        this.profile = profile
    }

    fun updateProfileData(profile: String?, nickname: String, email: String) {
        this.profile = profile
        this.nickname = nickname
        this.email = email
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateUsername(name: String) {
        this.username = name
    }

    fun updateNickname(nickname: String) {
        this.nickname = nickname
    }

    fun toOAuth2DTOUseFilter(): OAuth2Member {
        return OAuth2Member(
            userId = this.userId,
            username = this.username,
            authList = this.auths.toList(),
            nickname = this.nickname
        )
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Member) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

}