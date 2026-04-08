package com.example.boardproject_kt.service.member

import com.example.boardproject_kt.domain.dto.member.response.ProfileResponse
import com.example.boardproject_kt.domain.entity.Member
import com.example.boardproject_kt.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberDataService(
    private val memberRepository: MemberRepository
) {

    fun saveEntityAndFlush(member: Member) {
        memberRepository.save(member)
        memberRepository.flush()
    }

    fun findOAuthUserByUserId(userId: String): Member? =
        memberRepository.findOAuthUserByUserId(userId)

    fun findByUserId(userId: String): Member? =
        memberRepository.findByUserId(userId)

    fun findByNickname(nickname: String): Member? =
        memberRepository.findByNickname(nickname)

    fun getMemberProfileDataByUserId(userId: String): ProfileResponse? =
        memberRepository.getMemberProfileDataByUserId(userId)
}