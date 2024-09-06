package com.example.boardproject_kt_ver_default.useCase.member

import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.JoinRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.ProfileRequestDTO
import com.example.boardproject_kt_ver_default.exception.custom.CustomIOException
import com.example.boardproject_kt_ver_default.service.auth.PasswordEncodeService
import com.example.boardproject_kt_ver_default.service.imageBoard.file.ImageFileWriteService
import com.example.boardproject_kt_ver_default.service.member.MemberWriteService
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class MemberWriteUseCase(
    private val memberService: MemberWriteService,
    private val fileService: ImageFileWriteService,
    private val passwordEncodeService: PasswordEncodeService
) {
    private val log = logger()

    @Value("\${file.profile.path}")
    lateinit var filePath: String

    @Transactional(rollbackFor = [Exception::class])
    fun join(joinDTO: JoinRequestDTO, profileThumbnail: MultipartFile?): String {
        var thumbnailName: String? = null

        try{
            if(profileThumbnail != null)
                thumbnailName = fileService.saveFile(filePath, profileThumbnail)["imageName"]

            joinDTO.userPw = passwordEncodeService.encodeMemberPassword(joinDTO.userPw)

            return memberService.joinProc(joinDTO, thumbnailName)
        }catch (e: Exception) {
            if(thumbnailName != null)
                fileService.deleteFile(filePath, thumbnailName)

            e.printStackTrace()

            throw IllegalArgumentException("join proc error")
        }

    }

    @Transactional(rollbackFor = [Exception::class])
    fun patchProfile(profile: ProfileRequestDTO
                     , profileThumbnail: MultipartFile?
                     , deleteThumbnail: String?
                    , principal: Principal): String {

        var newThumbnail: String? = null

        try {
            if(profileThumbnail != null)
                newThumbnail = fileService.saveFile(filePath, profileThumbnail)["imageName"]

            val result = memberService.patchProfile(profile, newThumbnail, deleteThumbnail, principal)

            try {
                if(deleteThumbnail != null)
                    fileService.deleteFile(filePath, deleteThumbnail)
            }catch (e: Exception) {
                log.error("deleteFile Exception. file Name : $deleteThumbnail")
            }

            return result
        }catch (e: Exception) {
            log.error("patchProfile Error : ${e.message}")

            if(newThumbnail != null)
                fileService.deleteFile(filePath, newThumbnail)

            throw CustomIOException("patchProfile IOException")
        }

    }

}