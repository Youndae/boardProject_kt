package com.example.boardproject_kt_ver_default.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/board")
class HierarchicalBoardController(
    //board 관련 UseCase
    //responseFactory
) {

    /**
     * 게시글 리스트 조회
     *
     * @param
     * pageNum, keyword, searchType -> PageRequestDTO로 한번에?
     * , principal
     *
     * @return
     * ResponseEntity<ResponsePageableDTO<Any>>
     */
    @GetMapping("/")
    fun getBoardList() {

    }

    /**
     * 게시글 상세 조회
     *
     * @param
     * boardNo(PathVariable), principal
     *
     * @return
     * ResponseEntity<ResponseDetailAndModifyDTO<Any>>
     */
    @GetMapping("/{boardNo}")
    fun getDetail(@PathVariable boardNo: Long) {

    }

    /**
     * 게시글 작성
     *
     * @param
     * boardTitle, boardContent -> class
     * principal
     *
     * @return
     * ResponseEntity<Long>
     */
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postBoard() {

    }

    /**
     * 수정 게시글 데이터 조회
     *
     * @param
     * boardNo(PathVariable)
     * principal
     *
     * @return
     * ResponseEntity<ResponseDetailAndModifyDTO<Any>>
     */
    @GetMapping("/patch-detail/{boardNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getPatchDetail(@PathVariable boardNo: Long, principal: Principal) {

    }

    /**
     * 게시글 수정
     *
     * @param
     * boardNo(PathVariable)
     * , boardTitle
     * , boardContent
     *
     * @return
     * ResponseEntity<Long>
     */
    @PatchMapping("/{boardNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun patchBoard(@PathVariable boardNo: Long, principal: Principal){

    }

    /**
     * 게시글 삭제
     *
     * @param
     * boardNo(PathVariable)
     * principal
     *
     * @return
     * ResponseEntity<String>
     */
    @DeleteMapping("/{boardNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun deleteBoard(@PathVariable boardNo: Long, principal: Principal) {

    }

    /**
     * 답글 페이지 접근 시 데이터 요청
     *
     * @param
     * boardNo(PathVariable)
     * principal
     *
     * @return
     * ResponseEntity<Any>
     * 필요 데이터 -> 상위 글 groupNo, indent, upperNo
     */
    @GetMapping("/reply/{boardNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getReplyDetail(@PathVariable boardNo: Long, principal: Principal) {

    }

    /**
     * 답글 작성
     *
     * @param
     * boardTitle
     * , boardContent
     * , boardGroupNo
     * , boardIndent
     * , boardUpperNo
     * , principal
     *
     * @return
     * ResponseEntity<Long>
     */
    @PostMapping("/reply")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postReply() {

    }
}