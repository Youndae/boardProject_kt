package com.example.boardproject_kt.usecase.member

import com.example.boardproject_kt.domain.dto.member.response.ProfileResponse
import com.example.boardproject_kt.domain.enumuration.MemberCheckResult
import com.example.boardproject_kt.exception.CustomAuthenticationException
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.service.member.MemberDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.security.Principal

private val log = KotlinLogging.logger {  }

@Service
class MemberReadUseCase(
    private val memberDataService: MemberDataService
) {

    fun checkId(userId: String): MemberCheckResult =
        memberDataService.findByUserId(userId)?.let { MemberCheckResult.DUPLICATED }
            ?: MemberCheckResult.VALID

    fun checkNickname(nickname: String, principal: Principal?): MemberCheckResult {
        val member = memberDataService.findByNickname(nickname)
                        ?: return MemberCheckResult.VALID

        return when {
            principal != null && member.userId == principal.name -> MemberCheckResult.VALID
            else -> MemberCheckResult.DUPLICATED
        }
    }

    fun getProfile(userId: String): ProfileResponse {
        val profileResponse = memberDataService.getMemberProfileDataByUserId(userId)

        if(profileResponse == null) {
            log.error { "getProfile member is null. userId=$userId" }
            throw CustomAuthenticationException(ErrorCode.UNAUTHORIZED)
        }

        return profileResponse
    }
}