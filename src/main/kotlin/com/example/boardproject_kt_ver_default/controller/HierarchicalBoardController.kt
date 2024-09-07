package com.example.boardproject_kt_ver_default.controller

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardPostDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardReplyPostDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard.HierarchicalBoardReplyInfoDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.response.ResponseDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.response.ResponsePageableListDTO
import com.example.boardproject_kt_ver_default.domain.factory.ResponseFactory
import com.example.boardproject_kt_ver_default.useCase.hierarchicalBoard.HierarchicalBoardReadUseCase
import com.example.boardproject_kt_ver_default.useCase.hierarchicalBoard.HierarchicalBoardWriteUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/board")
class HierarchicalBoardController(
    private val boardReadUseCase: HierarchicalBoardReadUseCase,
    private val boardWriteUseCase: HierarchicalBoardWriteUseCase,
    private val responseFactory: ResponseFactory
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
    fun getBoardList(@RequestParam(value = "pageNum") pageNum: Int
                    , @RequestParam(value = "keyword", required = false) keyword: String?
                    , @RequestParam(value = "searchType", required = false) searchType: String?
                    , principal: Principal): ResponseEntity<ResponsePageableListDTO<HierarchicalBoardListDTO>> {

        val result = boardReadUseCase.getList(pageNum, keyword, searchType)

        return responseFactory.createPaginationList(result, principal)
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
    fun getDetail(@PathVariable boardNo: Long, principal: Principal): ResponseEntity<ResponseDetailDTO<HierarchicalBoardDetailDTO>> {

        val result = boardReadUseCase.getDetail(boardNo)

        return responseFactory.createDetailResponse(result, principal)
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
    fun postBoard(@RequestBody boardDTO: HierarchicalBoardPostDTO, principal: Principal): ResponseEntity<Long> {

        val result = boardWriteUseCase.postBoard(boardDTO, principal)

        return responseFactory.createLongResponse(result)
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
    fun getPatchDetail(@PathVariable boardNo: Long, principal: Principal): ResponseEntity<ResponseDetailDTO<HierarchicalBoardPatchDTO>> {

        val result = boardReadUseCase.getPatchDetail(boardNo, principal)

        return responseFactory.createDetailResponse(result, principal)
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
    fun patchBoard(@PathVariable boardNo: Long
                   , @RequestBody boardDTO: HierarchicalBoardPostDTO
                   , principal: Principal): ResponseEntity<Long> {

        val result = boardWriteUseCase.patchBoard(boardNo, boardDTO, principal)

        return responseFactory.createLongResponse(result)
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
    fun deleteBoard(@PathVariable boardNo: Long, principal: Principal): ResponseEntity<String> {

        val result = boardWriteUseCase.deleteBoard(boardNo, principal)

        return responseFactory.createStringResponse(result)
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
    fun getReplyDetail(@PathVariable boardNo: Long, principal: Principal): ResponseEntity<ResponseDetailDTO<HierarchicalBoardReplyInfoDTO>> {

        val result = boardReadUseCase.getReplyData(boardNo)

        return responseFactory.createDetailResponse(result, principal)
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
    fun postReply(@RequestBody boardDTO: HierarchicalBoardReplyPostDTO
                    , principal: Principal): ResponseEntity<Long> {

        val result = boardWriteUseCase.postReply(boardDTO, principal)

        return responseFactory.createLongResponse(result)
    }
}