package com.example.boardproject_kt_ver_default.controller

import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.JoinRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.LoginRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.ProfileRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.member.LoginStatusDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.member.ProfileResponseDTO
import com.example.boardproject_kt_ver_default.domain.factory.ResponseFactory
import com.example.boardproject_kt_ver_default.useCase.member.MemberReadUseCase
import com.example.boardproject_kt_ver_default.useCase.member.MemberWriteUseCase
import com.example.boardproject_kt_ver_default.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/member")
class MemberController(
    private val memberReadUseCase: MemberReadUseCase,
    private val memberWriteUseCase: MemberWriteUseCase,
    private val responseFactory: ResponseFactory
) {

    private val log = logger()
    /**
     * 로그인 체크 - React에서 로그인 여부 확인 시 요청
     *
     * @param
     * principal
     *
     * @return
     * ResponseEntity<Any>
     */
    @GetMapping("/check-login")
    fun checkLogin(principal: Principal?): ResponseEntity<LoginStatusDTO> {
        val responseDTO = LoginStatusDTO(principal)

        return ResponseEntity.status(HttpStatus.OK)
            .body(responseDTO)
    }

    /**
     * 회원 가입
     *
     * @param
     * joinDTO
     * , profileThumbnail
     *
     * @return
     * ResponseEntity<String> || String
     */
    @PostMapping("/join")
    fun joinProc(@RequestPart joinDTO: JoinRequestDTO
    , @RequestParam(value = "profileThumbnail", required = false) profileThumbnail: MultipartFile?): ResponseEntity<String> {

        val result = memberWriteUseCase.join(joinDTO, profileThumbnail)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 가입시 아이디 체크
     *
     * @param
     * userId(RequestParam)
     *
     * @return
     * ResponseEntity<String> || String
     */
    @GetMapping("/check-id")
    fun checkUserId(@RequestParam("userId") userId: String): ResponseEntity<String> {

        val result = memberReadUseCase.checkUserId(userId)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 닉네임 체크
     *
     * @param
     * nickname(RequestParam)
     * , principal -> 가입 말고 정보 수정 시 체크하기 위함
     *
     * @return
     * ResponseEntity<String> || String
     */
    @GetMapping("/check-nickname")
    fun checkNickname(@RequestParam("nickname") nickname: String, principal: Principal?): ResponseEntity<String> {
        val result = memberReadUseCase.checkNickname(nickname, principal)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 로그인
     *
     * @param
     * memberDTO -> id, pw
     * , request
     * , response
     *
     * @return
     * ResponseEntity<String>
     */
    @PostMapping("/login")
    fun login(@RequestBody member: LoginRequestDTO
                , request: HttpServletRequest
                , response: HttpServletResponse): ResponseEntity<String> {
        val result = memberReadUseCase.login(member, request, response)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 로그아웃
     *
     * @param
     * request, response, principal
     *
     * @return
     * ResponseEntity<String> || String
     */
    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun logout(principal: Principal
                , request: HttpServletRequest
                , response: HttpServletResponse): ResponseEntity<String> {

        val result = memberReadUseCase.logout(principal, request, response)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 정보 수정
     *
     * @param
     * nickname, profileThumbnail, deleteProfile, principal
     *
     * @return
     * ResponseEntity<String> || String
     */
    @PatchMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun patchProfile(@RequestParam("nickname") nickname: String
                    , @RequestParam(value = "profileThumbnail", required = false) profileThumbnail: MultipartFile?
                    , @RequestParam(value = "deleteProfile", required = false) deleteProfile: String?
                    , principal: Principal): ResponseEntity<String> {

        log.info("patchProfile nickname : {}", nickname)

        val result = memberWriteUseCase.patchProfile(nickname, profileThumbnail, deleteProfile, principal)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 수정할 정보 데이터 요청
     *
     * @param
     * principal
     *
     * @return
     * ResponseEntity<ProfileResponseDTO>
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getProfile(principal: Principal): ResponseEntity<ProfileResponseDTO> {
        val result: ProfileResponseDTO = memberReadUseCase.getProfile(principal)

        return ResponseEntity.status(HttpStatus.OK)
                    .body(result)
    }
}