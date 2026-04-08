package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long>, CustomMemberRepository {
}