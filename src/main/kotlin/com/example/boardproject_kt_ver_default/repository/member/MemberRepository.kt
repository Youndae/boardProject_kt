package com.example.boardproject_kt_ver_default.repository.member

import com.example.boardproject_kt_ver_default.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository: JpaRepository<Member, String> {

    fun findByUserId(userId: String): Member

    fun findByNickname(nickname: String): Member?

    @Query(value = "SELECT m " +
            "FROM Member m " +
            "WHERE m.userId = ?1 " +
            "AND m.provider = 'local'")
    fun findByLocalUserId(userId: String): Member?

    @Query(value = "SELECT m " +
            "FROM Member m " +
            "WHERE m.userId = ?1 " +
            "AND m.provider = ?2")
    fun findByOAuthUserId(userId: String, provider: String): Member?
}