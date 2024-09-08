package com.example.boardproject_kt_ver_default.service.hierarchicalBoard

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardPostDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardReplyPostDTO
import com.example.boardproject_kt_ver_default.domain.entity.HierarchicalBoard
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException
import com.example.boardproject_kt_ver_default.repository.hierarchicalBoard.HierarchicalBoardRepository
import com.example.boardproject_kt_ver_default.domain.enumuration.Result
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class HierarchicalBoardWriteService(
    val boardRepository: HierarchicalBoardRepository
) {

    private val log = logger()

    fun postBoard(boardDTO: HierarchicalBoardPostDTO, member: Member): Long {
        val board = HierarchicalBoard(
                        member = member,
                        boardTitle = boardDTO.boardTitle,
                        boardContent = boardDTO.boardContent
                    )

        boardRepository.save(board)
        board.setInsertionOtherData()
        boardRepository.save(board)

        return board.boardNo
    }

    fun patchBoard(boardNo: Long, boardDTO: HierarchicalBoardPostDTO, member: Member): Long {
        val board: HierarchicalBoard = boardRepository.findById(boardNo)
                                            .orElseThrow { NoSuchElementException("Patch board data not found") }

        if(board.member.userId != member.userId)
            throw CustomAccessDeniedException("patch board AccessDenied")

        board.setPatchData(boardDTO)
        boardRepository.save(board)

        return boardNo
    }

    fun postReply(boardDTO: HierarchicalBoardReplyPostDTO, member: Member): Long {
        val board = HierarchicalBoard(
                        member = member,
                        boardTitle = boardDTO.boardTitle,
                        boardContent = boardDTO.boardContent
                    )
        boardRepository.save(board)
        board.setInsertionReplyData(boardDTO)
        boardRepository.save(board)

        return board.boardNo
    }

    fun deleteBoard(boardNo: Long, principal: Principal): String {
        val board: HierarchicalBoard = boardRepository.findById(boardNo)
                                        .orElseThrow{ NoSuchElementException("Delete board data not found") }

        if(board.member.userId != principal.name)
            throw CustomAccessDeniedException("Delete board AccessDenied")

        if(board.boardIndent == 0)
            boardRepository.deleteByBoardGroupNo(boardNo)
        else {
            val groupList: List<HierarchicalBoard> = boardRepository.findAllByBoardGroupNo(board.boardGroupNo!!)
            val deleteBoardNoList: List<Long> = searchDeleteIdList(groupList, boardNo, board.boardIndent!!)
            boardRepository.deleteAllById(deleteBoardNoList)
        }

        return Result.SUCCESS.resultMessage
    }

    fun searchDeleteIdList(groupList: List<HierarchicalBoard>, deleteNo: Long, indent: Int): List<Long> {

        return groupList.filter { v ->
                                    val upperArr = v.boardUpperNo!!.split(",")
                                    upperArr.size > indent && upperArr[indent] == deleteNo.toString()
                                }
                                .map { it.boardNo }
    }
}