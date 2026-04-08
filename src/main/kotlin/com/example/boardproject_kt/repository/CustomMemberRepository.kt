package com.example.boardproject_kt.repository

import com.example.boardproject_kt.domain.dto.member.response.ProfileResponse
import com.example.boardproject_kt.domain.entity.Member

interface CustomMemberRepository {

    fun findByUserId(userId: String): Member?

    fun findByLoginUserId(userId: String): Member?

    fun findOAuthUserByUserId(userId: String): Member?

    fun findByNickname(nickname: String): Member?

    fun getMemberProfileDataByUserId(userId: String): ProfileResponse?
}