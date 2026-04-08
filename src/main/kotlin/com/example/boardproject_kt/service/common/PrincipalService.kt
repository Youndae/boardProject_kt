package com.example.boardproject_kt.service.common

import com.example.boardproject_kt.domain.entity.Member
import com.example.boardproject_kt.exception.CustomAccessDeniedException
import com.example.boardproject_kt.exception.CustomNotFoundException
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.repository.MemberRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {  }

@Service
class PrincipalService(
    private val memberRepository: MemberRepository
) {

    fun getMemberByUserId(userId: String): Member {
        val member = memberRepository.findByUserId(userId)

        if(member == null) {
            log.warn { "PrincipalService.getMemberByUserId :: member is null. userId=$userId" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST, "member is null")
        }

        return member
    }

    fun validateUser(writer: String, userId: String) {
        if(writer != userId)
            throw CustomAccessDeniedException(ErrorCode.FORBIDDEN)
    }
}