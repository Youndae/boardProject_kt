package com.example.boardproject_kt_ver_default.useCase.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardPostDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardReplyPostDTO
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.service.auth.PrincipalReadService
import com.example.boardproject_kt_ver_default.service.hierarchicalBoard.HierarchicalBoardWriteService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class HierarchicalBoardWriteUseCase(
    private val boardWriteService: HierarchicalBoardWriteService,
    private val principalReadService: PrincipalReadService
) {
    fun postBoard(boardDTO: HierarchicalBoardPostDTO, principal: Principal): Long {
        val member: Member = principalReadService.getMemberEntity(principal)

        return boardWriteService.postBoard(boardDTO, member)
    }

    fun patchBoard(boardNo: Long, boardDTO: HierarchicalBoardPostDTO, principal: Principal): Long {
        val member: Member = principalReadService.getMemberEntity(principal)

        return boardWriteService.patchBoard(boardNo, boardDTO, member)
    }

    fun deleteBoard(boardNo: Long, principal: Principal): String {

        return boardWriteService.deleteBoard(boardNo, principal)
    }

    fun postReply(boardDTO: HierarchicalBoardReplyPostDTO, principal: Principal): Long {
        val member: Member = principalReadService.getMemberEntity(principal)

        return boardWriteService.postReply(boardDTO, member)
    }


}