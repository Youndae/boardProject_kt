package com.example.boardproject_kt.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "auth")
class Auth(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val member: Member,

    @Column(nullable = false)
    val auth: String
) {

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Auth) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}