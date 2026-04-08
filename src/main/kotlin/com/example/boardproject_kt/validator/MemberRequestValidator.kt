package com.example.boardproject_kt.validator

import com.example.boardproject_kt.domain.dto.member.request.JoinRequest
import com.example.boardproject_kt.domain.dto.member.request.OAuthJoinRequest
import com.example.boardproject_kt.domain.dto.member.request.UpdateProfileRequest
import com.example.boardproject_kt.exception.CustomInvalidJoinPolicyException
import com.example.boardproject_kt.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {  }

@Component
class MemberRequestValidator {

    companion object {
        private val USER_ID_PATTERN = Regex("""^[A-Za-z0-9]{5,15}$""")
        private val PASSWORD_PATTERN = Regex("""^(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-])(?=.*[0-9]).{8,16}$""")
        private val EMAIL_PATTERN = Regex("""^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$""")
        private val NICKNAME_PATTERN = Regex("""^[가-힣a-zA-Z0-9]{2,15}$""")
    }

    private fun validateUserId(userId: String?) {
        if(userId == null || !USER_ID_PATTERN.matches(userId))
            throw CustomInvalidJoinPolicyException(ErrorCode.BAD_REQUEST, "userId Invalid")
    }

    private fun validatePassword(password: String?) {
        if(password == null || !PASSWORD_PATTERN.matches(password))
            throw CustomInvalidJoinPolicyException(ErrorCode.BAD_REQUEST, "password Invalid")
    }

    private fun validateEmail(email: String?) {
        if(email == null || !EMAIL_PATTERN.matches(email))
            throw CustomInvalidJoinPolicyException(ErrorCode.BAD_REQUEST, "email Invalid")
    }

    private fun validateUserName(userName: String?) {
        if(userName == null || userName.length < 2)
            throw CustomInvalidJoinPolicyException(ErrorCode.BAD_REQUEST, "userName Invalid")
    }

    private fun validateNickname(nickname: String?) {
        if(nickname == null || !NICKNAME_PATTERN.matches(nickname))
            throw CustomInvalidJoinPolicyException(ErrorCode.BAD_REQUEST, "nickname Invalid")
    }

    fun validateJoinRequest(request: JoinRequest) {
        log.info { "MemberRequestValidator.validateJoinRequest" }
        validateUserId(request.userId)
        validatePassword(request.password)
        validateUserName(request.userName)
        validateNickname(request.nickname)
        validateEmail(request.email)
    }

    fun validateOAuthRequest(request: OAuthJoinRequest) {
        log.info { "MemberRequestValidator.validateOAuthRequest" }
        validateNickname(request.nickname)
    }

    fun validateUpdateProfile(request: UpdateProfileRequest) {
        log.info { "MemberRequestValidator.validateUpdateProfile" }
        validateNickname(request.nickname)
        validateEmail(request.email)
    }
}