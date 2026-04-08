package com.example.boardproject_kt.usecase.member

import com.example.boardproject_kt.auth.service.TokenProvider
import com.example.boardproject_kt.domain.dto.member.request.JoinRequest
import com.example.boardproject_kt.domain.dto.member.request.OAuthJoinRequest
import com.example.boardproject_kt.domain.dto.member.request.UpdateProfileRequest
import com.example.boardproject_kt.exception.CustomAccessDeniedException
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.mapper.MemberMapper
import com.example.boardproject_kt.properties.CookieProperties
import com.example.boardproject_kt.service.file.FileDataService
import com.example.boardproject_kt.service.member.MemberDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.WebUtils

private val log = KotlinLogging.logger {  }

@Service
class MemberWriteUseCase(
    private val cookieProperties: CookieProperties,
    private val tokenProvider: TokenProvider,
    private val memberDataService: MemberDataService,
    private val memberMapper: MemberMapper,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val fileDataService: FileDataService,
    @Value("#{filePath['file.profile.path']}")
    private val profilePath: String
) {

    fun postLogout(request: HttpServletRequest, response: HttpServletResponse, userId: String) {
        val inoValue = WebUtils.getCookie(request, cookieProperties.ino.header)!!.value

        try {
            tokenProvider.deleteTokenData(userId, inoValue)
        } catch(e: Exception) {
            log.warn { "Failed to delete Redis token for user=$userId, ino=${inoValue}. Error=$e" }
        }finally {
            tokenProvider.deleteTokenCookie(response)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun register(request: JoinRequest) {
        val memberEntity = memberMapper.toFullEntity(request, passwordEncoder)
        var saveImageName: String? = null
        try {

            if(request.hasProfile()) {
                val profile = fileDataService.profileImageSave(request.profile!!)
                saveImageName = profile
                memberEntity.updateProfile(profile)
            }

            memberDataService.saveEntityAndFlush(memberEntity)
        }catch(e: Exception) {
            if(saveImageName != null)
                fileDataService.deleteFile(profilePath, saveImageName)

            throw IllegalArgumentException("register Error=$e")
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun oAuthJoin(
        request: OAuthJoinRequest,
        userId: String
    ) {
        val memberEntity = memberDataService.findOAuthUserByUserId(userId)

        if(memberEntity == null) {
            log.warn { "MemberWriteUseCase.oAuthJoin :: join oauth member is null. userId=$userId" }
            throw CustomAccessDeniedException(ErrorCode.FORBIDDEN)
        }

        memberEntity.updateNickname(request.nickname)

        var saveImageName: String? = null
        try {
            if(request.hasProfile()) {
                val profile = fileDataService.profileImageSave(request.profile!!)
                saveImageName = profile
                memberEntity.updateProfile(profile)
            }
        }catch(e: Exception) {
            if(saveImageName != null)
                fileDataService.deleteFile(profilePath, saveImageName)

            throw IllegalArgumentException("oAuth patch profile error=$e")
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateProfile(
        request: UpdateProfileRequest,
        userId: String
    ) {
        val member = memberDataService.findByUserId(userId)!!

        var saveImageName: String? = null

        try {
            var profileThumbnail: String? = null
            if(request.hasProfile()) {
                profileThumbnail = fileDataService.profileImageSave(request.profile!!)
                saveImageName = profileThumbnail
            }

            if(!request.hasProfile() && !request.hasDeleteProfile())
                profileThumbnail = member.profile
            else if(request.hasDeleteProfile())
                fileDataService.deleteFile(profilePath, request.deleteProfile!!)

            member.updateProfileData(profileThumbnail, request.nickname, request.email)

            memberDataService.saveEntityAndFlush(member)
        }catch(e: Exception) {
            if(saveImageName != null)
                fileDataService.deleteFile(profilePath, saveImageName)

            throw IllegalArgumentException("update profile error=$e")
        }
    }
}