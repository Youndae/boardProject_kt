package com.example.boardproject_kt_ver_default.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.ToString

@Entity
class Auth(
    var auth: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var authNo: Long? = null

    @ManyToOne
    @JoinColumn(name = "userId")
    lateinit var member: Member

    fun memberSet(member: Member) {
        this.member = member
    }
}