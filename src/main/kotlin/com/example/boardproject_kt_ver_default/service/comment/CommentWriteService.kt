package com.example.boardproject_kt_ver_default.service.comment

import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentInsertDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentReplyInsertDTO
import com.example.boardproject_kt_ver_default.domain.entity.Comment
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.example.boardproject_kt_ver_default.repository.comment.CommentRepository
import com.example.boardproject_kt_ver_default.repository.hierarchicalBoard.HierarchicalBoardRepository
import com.example.boardproject_kt_ver_default.repository.imageBoard.ImageBoardRepository
import com.example.boardproject_kt_ver_default.service.auth.PrincipalReadService
import org.springframework.stereotype.Service
import java.security.Principal
import com.example.boardproject_kt_ver_default.domain.enumuration.Result
import com.example.boardproject_kt_ver_default.exception.custom.CustomAccessDeniedException

@Service
class CommentWriteService(
    private val principalReadService: PrincipalReadService,
    private val hierarchicalBoardRepository: HierarchicalBoardRepository,
    private val imageBoardRepository: ImageBoardRepository,
    private val commentRepository: CommentRepository
) {
    fun postBoardComment(boardNo: Long, commentDTO: CommentInsertDTO, principal: Principal): String {
        val member: Member = getMemberFromPrincipal(principal)
        val comment: Comment = createCommentFromDTO(commentDTO, member)
        findHierarchicalBoard(boardNo, comment)
        saveComment(comment)

        return Result.SUCCESS.resultMessage
    }

    fun postImageBoardComment(imageNo: Long, commentDTO: CommentInsertDTO, principal: Principal): String {
        val member: Member = getMemberFromPrincipal(principal)
        val comment: Comment = createCommentFromDTO(commentDTO, member)
        findImageBoard(imageNo, comment)
        saveComment(comment)

        return Result.SUCCESS.resultMessage
    }

    fun postBoardCommentReply(boardNo: Long, commentDTO: CommentReplyInsertDTO, principal: Principal): String {
        val member: Member = getMemberFromPrincipal(principal)
        val comment: Comment = createCommentFromDTO(commentDTO, member)
        findHierarchicalBoard(boardNo, comment)
        saveReplyComment(comment, commentDTO)

        return Result.SUCCESS.resultMessage
    }

    fun postImageBoardCommentReply(imageNo: Long, commentDTO: CommentReplyInsertDTO, principal: Principal): String {
        val member: Member = getMemberFromPrincipal(principal)
        val comment: Comment = createCommentFromDTO(commentDTO, member)
        findImageBoard(imageNo, comment)
        saveReplyComment(comment, commentDTO)

        return Result.SUCCESS.resultMessage
    }

    private fun getMemberFromPrincipal(principal: Principal): Member {
        return principalReadService.checkPrincipal(principal).toMemberEntity()
    }

    private fun createCommentFromDTO(commentDTO: CommentInsertDTO, member: Member): Comment {
        return commentDTO.toEntity(member)
    }

    private fun findHierarchicalBoard(boardNo: Long, comment: Comment) {
        val board = hierarchicalBoardRepository.findById(boardNo).orElseThrow{ IllegalArgumentException("post comment Exception. board not found") }
        comment.hierarchicalBoard = board
        comment.imageBoard = null
    }

    private fun findImageBoard(imageNo: Long, comment: Comment) {
        val board = imageBoardRepository.findById(imageNo).orElseThrow{ IllegalArgumentException("post comment Exception. imageBoard not found") }
        comment.hierarchicalBoard = null
        comment.imageBoard = board
    }

    private fun saveComment(comment: Comment) {
        commentRepository.save(comment)
        comment.setCommentInsertPatchData()
        commentRepository.save(comment)
    }

    private fun saveReplyComment(comment: Comment, commentDTO: CommentReplyInsertDTO) {
        commentRepository.save(comment)
        comment.setCommentReplyPatchData(commentDTO)
        commentRepository.save(comment)
    }

    fun deleteComment(commentNo: Long, principal: Principal): String {
        val member = getMemberFromPrincipal(principal)
        val comment = commentRepository.findById(commentNo).orElseThrow{ IllegalArgumentException("delete comment Exception. comment record not found") }

        if(comment.member.userId != member.userId)
            throw CustomAccessDeniedException("delete comment AccessDeniedException")

        commentRepository.deleteById(commentNo)

        return Result.SUCCESS.resultMessage
    }
}