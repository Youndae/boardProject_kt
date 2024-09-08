package com.example.boardproject_kt_ver_default.controller

import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentInsertDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentReplyInsertDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.comment.CommentDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.response.ResponsePageableListDTO
import com.example.boardproject_kt_ver_default.domain.factory.ResponseFactory
import com.example.boardproject_kt_ver_default.useCase.comment.CommentReadUseCase
import com.example.boardproject_kt_ver_default.useCase.comment.CommentWriteUseCase
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/comment")
class CommentController(
    private val commentReadUseCase: CommentReadUseCase,
    private val commentWriteUseCase: CommentWriteUseCase,
    private val responseFactory: ResponseFactory
) {

    private val log = logger()

    /**
     * 계층형 게시판 댓글 리스트 조회.
     *
     * @param
     * boardNo, pageNum, principal
     *
     * @return
     * ResponseEntity<ResponsePageableDTO<Any>>
     */
    @GetMapping("/board")
    fun getBoardCommentList(@RequestParam(value = "boardNo") boardNo: Long
                            , @RequestParam("pageNum") pageNum: Int
                            , principal: Principal?): ResponseEntity<ResponsePageableListDTO<CommentDTO>> {

        val result = commentReadUseCase.getBoardCommentList(boardNo, pageNum)

        log.info("CommentController::getList : {}", result)

        return responseFactory.createPaginationList(result, principal)
    }

    /**
     * 이미지 게시판 댓글 리스트 조회.
     *
     * @param
     * imageNo, pageNum, principal
     *
     * @return
     * ResponseEntity<ResponsePageableDTO<Any>>
     */
    @GetMapping("/image")
    fun getImageBoardCommentList(@RequestParam(value = "imageNo") imageNo: Long
                                 , @RequestParam("pageNum") pageNum: Int
                                 , principal: Principal?): ResponseEntity<ResponsePageableListDTO<CommentDTO>> {
        val result = commentReadUseCase.getImageCommentList(imageNo, pageNum)

        return responseFactory.createPaginationList(result, principal)
    }

    /**
     * 계층형 게시판 댓글 작성
     *
     * @param
     * boardNo(PathVariable)
     * , commentContent(class || val)
     * , principal
     *
     * @return
     * ResponseEntity<String>
     */
    @PostMapping("/board/{boardNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postBoardComment(@PathVariable boardNo: Long
                         , @RequestBody commentDTO: CommentInsertDTO
                        ,principal: Principal): ResponseEntity<String> {
        val result = commentWriteUseCase.postBoardComment(boardNo, commentDTO, principal)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 이미지 게시판 댓글 작성
     *
     * @param
     * boardNo(PathVariable)
     * , commentContent(class || val)
     * , principal
     *
     * @return
     * ResponseEntity<String>
     */
    @PostMapping("/image/{boardNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postImageBoardComment(@PathVariable boardNo: Long
                              , @RequestBody commentDTO: CommentInsertDTO
                            , principal: Principal): ResponseEntity<String> {
        val result = commentWriteUseCase.postImageBoardComment(boardNo, commentDTO, principal)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 댓글 삭제
     *
     * @param
     * commentNo(PathVariable), principal
     *
     * @return
     * ResponseEntity<String>
     */
    @DeleteMapping("/{commentNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun deleteComment(@PathVariable commentNo: Long
                    , principal: Principal): ResponseEntity<String> {

        val result = commentWriteUseCase.deleteComment(commentNo, principal)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 계층형 게시판 대댓글 작성
     *
     * @param
     * boardNo(PathVariable)
     * , commentContent
     * , commentGroupNo
     * , commentIndent
     * , commentUpperNo
     *
     * @return
     * ResponseEntity<String>
     */
    @PostMapping("/board/{boardNo}/reply")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postBoardCommentReply(@PathVariable boardNo: Long
                            , @RequestBody commentDTO: CommentReplyInsertDTO
                            , principal: Principal): ResponseEntity<String> {
        val result = commentWriteUseCase.postBoardCommentReply(boardNo, commentDTO, principal)

        return responseFactory.createStringResponse(result)
    }

    /**
     * 이미지 게시판 대댓글 작성
     *
     * @param
     * boardNo(PathVariable)
     * , commentContent
     * , commentGroupNo
     * , commentIndent
     * , commentUpperNo
     *
     * @return
     * ResponseEntity<String>
     */
    @PostMapping("/image/{boardNo}/reply")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postImageBoardCommentReply(@PathVariable boardNo: Long
                                   , @RequestBody commentDTO: CommentReplyInsertDTO
                                   , principal: Principal): ResponseEntity<String> {
        val result = commentWriteUseCase.postImageBoardCommentReply(boardNo, commentDTO, principal)

        return responseFactory.createStringResponse(result)
    }
}