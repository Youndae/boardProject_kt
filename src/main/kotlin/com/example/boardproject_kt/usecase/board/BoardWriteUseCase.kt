package com.example.boardproject_kt.usecase.board

import com.example.boardproject_kt.domain.dto.board.request.BoardReplyRequest
import com.example.boardproject_kt.domain.dto.board.request.BoardRequest
import com.example.boardproject_kt.service.board.BoardDataService
import com.example.boardproject_kt.service.common.PrincipalService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardWriteUseCase(
    private val boardDataService: BoardDataService,
    private val principalService: PrincipalService
) {

    @Transactional(rollbackFor = [Exception::class])
    fun postBoard(request: BoardRequest, userId: String): Long {
        val memberEntity = principalService.getMemberByUserId(userId)

        val boardEntity = request.toEntity(memberEntity)

        val board = boardDataService.saveBoard(boardEntity)
        board.initializeRootPath()

        return board.id!!
    }

    @Transactional(rollbackFor = [Exception::class])
    fun patchBoard(request: BoardRequest, id: Long, userId: String): Long {
        val targetBoard = boardDataService.findById(id)

        principalService.validateUser(targetBoard.member.userId, userId)
        targetBoard.updatePatchData(request)

        return id
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteBoard(id: Long, userId: String) {
        val targetBoard = boardDataService.findById(id)
        principalService.validateUser(targetBoard.member.userId, userId)

        if(targetBoard.indent == 0)
            boardDataService.deleteByGroupNo(id)
        else {
            val selfUpperNo = targetBoard.upperNo
            val childUpperNo = "${targetBoard.upperNo},%"

            boardDataService.deleteByPath(targetBoard.groupNo!!, selfUpperNo!!, childUpperNo)
        }
    }

    fun postBoardReply(targetId: Long, request: BoardReplyRequest, userId: String): Long {
        val targetBoard = boardDataService.findById(targetId)
        val memberEntity = principalService.getMemberByUserId(userId)

        val boardEntity = request.toEntity(memberEntity, targetBoard)
        val board = boardDataService.saveBoard(boardEntity)
        board.initializeReplyPath(targetBoard.upperNo!!)

        return board.id!!
    }
}