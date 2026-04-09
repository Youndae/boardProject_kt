package com.example.boardproject_kt.controller

import com.example.boardproject_kt.domain.dto.member.request.JoinRequest
import com.example.boardproject_kt.domain.dto.member.request.OAuthJoinRequest
import com.example.boardproject_kt.domain.dto.member.request.UpdateProfileRequest
import com.example.boardproject_kt.domain.dto.member.response.MemberStatusResponse
import com.example.boardproject_kt.domain.dto.member.response.ProfileResponse
import com.example.boardproject_kt.domain.dto.response.ApiResponse
import com.example.boardproject_kt.domain.enums.MemberCheckResult
import com.example.boardproject_kt.service.member.AuthContextService
import com.example.boardproject_kt.usecase.file.FileReadUseCase
import com.example.boardproject_kt.usecase.member.MemberReadUseCase
import com.example.boardproject_kt.usecase.member.MemberWriteUseCase
import com.example.boardproject_kt.validator.MemberRequestValidator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberReadUseCase: MemberReadUseCase,
    private val memberWriteUseCase: MemberWriteUseCase,
    private val fileReadUseCase: FileReadUseCase,
    private val memberRequestValidator: MemberRequestValidator,
    private val authContextService: AuthContextService,
) {

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    fun checkLogin(
        authentication: Authentication
    ): ResponseEntity<ApiResponse<MemberStatusResponse>> {
        val body = authContextService.getMemberStatus(authentication)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<Unit> {
        memberWriteUseCase.postLogout(request, response, userId)

        return ResponseEntity.noContent().build()
    }

    @PostMapping(value = ["/join"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun joinProc(
        @ModelAttribute joinRequest: JoinRequest
    ): ResponseEntity<Unit> {
        memberRequestValidator.validateJoinRequest(joinRequest)
        memberWriteUseCase.register(joinRequest)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping(value = ["/oauth/join/profile"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun postOAuthProfile(
        @ModelAttribute request: OAuthJoinRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        memberRequestValidator.validateOAuthRequest(request)
        memberWriteUseCase.oAuthJoin(request, principal.name)

        return ResponseEntity.ok().build()
    }

    @GetMapping("/check-id/{userId}")
    fun checkUserId(
        @PathVariable(name = "userId") userId: String
    ): ResponseEntity<ApiResponse<Unit>> {
        val message: MemberCheckResult = memberReadUseCase.checkId(userId)

        return ResponseEntity.ok(ApiResponse.success(message = message.message))
    }

    @GetMapping("/check-nickname/{nickname}")
    fun checkNickname(
        @PathVariable(name = "nickname") nickname: String,
        principal: Principal?
    ): ResponseEntity<ApiResponse<Unit>> {
        val message: MemberCheckResult = memberReadUseCase.checkNickname(nickname, principal)

        return if(message == MemberCheckResult.VALID)
            ResponseEntity.ok(ApiResponse.success(message = message.message))
        else
            ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.success(message = message.message))
    }

    @PatchMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    fun updateProfile(
        @ModelAttribute request: UpdateProfileRequest,
        principal: Principal
    ): ResponseEntity<Unit> {
        memberRequestValidator.validateUpdateProfile(request)
        memberWriteUseCase.updateProfile(request, principal.name)

        return ResponseEntity.noContent().build()
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    fun getProfile(
        principal: Principal
    ): ResponseEntity<ApiResponse<ProfileResponse>> {
        val body = memberReadUseCase.getProfile(principal.name)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @GetMapping("/display/{imageName}")
    fun getFile(
        @PathVariable(name = "imageName") imageName: String
    ): ResponseEntity<ByteArray> {
        return fileReadUseCase.getProfileImageDisplay(imageName)
    }
}