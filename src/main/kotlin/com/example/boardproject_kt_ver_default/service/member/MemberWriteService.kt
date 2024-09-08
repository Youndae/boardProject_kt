package com.example.boardproject_kt_ver_default.service.member

import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.JoinRequestDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.member.ProfileRequestDTO
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.repository.member.MemberRepository
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.stereotype.Service
import com.example.boardproject_kt_ver_default.domain.enumuration.Result
import java.security.Principal

@Service
class MemberWriteService(
    private val memberRepository: MemberRepository
) {

    private val log = logger()

    fun joinProc(joinDTO: JoinRequestDTO, profileThumbnail: String?): String {
        val member = joinDTO.toEntity(profileThumbnail)
        member.addAuth()
        memberRepository.save(member)

        return Result.SUCCESS.resultMessage
    }

    fun patchProfile(nickname: String
                     , newThumbnail: String?
                     , deleteThumbnail: String?
                     , principal: Principal): String {

        val member: Member = memberRepository.findByUserId(principal.name)

        member.nickName = nickname

        if(newThumbnail != null)
            member.profileThumbnail = newThumbnail
        else if(deleteThumbnail == member.profileThumbnail)
            member.profileThumbnail = null

        memberRepository.save(member)

        return Result.SUCCESS.resultMessage
    }
}