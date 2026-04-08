package com.example.boardproject_kt.usecase.comment

import com.example.boardproject_kt.domain.dto.comment.request.CommentRequest
import com.example.boardproject_kt.domain.entity.Comment
import com.example.boardproject_kt.exception.CustomNotFoundException
import com.example.boardproject_kt.exception.ErrorCode
import com.example.boardproject_kt.service.board.BoardDataService
import com.example.boardproject_kt.service.comment.CommentDataService
import com.example.boardproject_kt.service.common.PrincipalService
import com.example.boardproject_kt.service.imageBoard.ImageBoardDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {  }

@Service
class CommentWriteUseCase(
    private val commentDataService: CommentDataService,
    private val boardDataService: BoardDataService,
    private val imageBoardDataService: ImageBoardDataService,
    private val principalService: PrincipalService,
) {

    @Transactional(rollbackFor = [Exception::class])
    fun postBoardComment(
        targetBoardId: Long,
        request: CommentRequest,
        userId: String
    ) {
        val memberEntity = principalService.getMemberByUserId(userId)
        val targetBoard = boardDataService.findById(targetBoardId)
        val comment = request.toEntity(memberEntity = memberEntity, boardEntity = targetBoard)
        saveComment(comment)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun postImageBoardComment(
        targetBoardId: Long,
        request: CommentRequest,
        userId: String
    ) {
        val memberEntity = principalService.getMemberByUserId(userId)
        val targetBoard = imageBoardDataService.findById(targetBoardId)
        val comment = request.toEntity(memberEntity = memberEntity, imageBoardEntity = targetBoard)
        saveComment(comment)
    }

    private fun saveComment(comment: Comment) {
        val saveCommentEntity = commentDataService.saveComment(comment)
        saveCommentEntity.initializeRootPath()
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteComment(
        id: Long,
        userId: String
    ) {
        val comment = commentDataService.findNotDeleteCommentById(id)

        if(comment == null) {
            log.warn { "CommentWriteUseCase.deleteComment :: deleteComment is null or is deleted object. id=$id" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST)
        }

        principalService.validateUser(comment.member.userId, userId)
        commentDataService.deleteComment(comment)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun postReplyComment(
        targetCommentId: Long,
        request: CommentRequest,
        userId: String
    ) {
        val targetComment = commentDataService.findNotDeleteCommentById(targetCommentId)
        if(targetComment == null) {
            log.warn { "CommentWriteUseCase.postReplyComment :: targetComment is null or is deleted object. id=$targetCommentId" }
            throw CustomNotFoundException(ErrorCode.BAD_REQUEST)
        }

        val memberEntity = principalService.getMemberByUserId(userId)

        val commentEntity = Comment(
            member = memberEntity,
            board = targetComment.board,
            imageBoard = targetComment.imageBoard,
            content = request.content,
            groupNo = targetComment.groupNo,
            indent = targetComment.indent + 1
        )

        val comment = commentDataService.saveComment(commentEntity)
        comment.initializeReplyPath(targetComment.upperNo!!)
    }
}