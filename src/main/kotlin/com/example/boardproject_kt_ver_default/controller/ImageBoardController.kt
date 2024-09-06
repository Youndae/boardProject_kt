package com.example.boardproject_kt_ver_default.controller

import org.springframework.http.MediaType
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
@RequestMapping("/image-board")
class ImageBoardController(
    //image UseCase
    //responseFactory
) {

    /**
     * 리스트 조회
     *
     * @param
     * pageNum, keyword, searchType -> PageDTO로 처리?
     * principal
     *
     * @return
     * ResponseEntity<ResponsePageableDTO<Any>>
     *
     */
    @GetMapping("/")
    fun getList() {

    }

    /**
     * 상세 페이지
     *
     * @param
     * imageNo(PathVariable)
     * principal
     *
     * @return
     * ResponseEntity<ResponseDetailAndModifyDTO<Any>>
     */
    @GetMapping("/{imageNo}")
    fun getDetail(@PathVariable imageNo: Long, principal: Principal) {

    }

    /**
     * 게시글 작성
     *
     * @param
     * imageFiles
     * , imageTitle
     * , imageContent
     *
     * @return
     * ResponseEntity<Long>
     */
    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun postBoard() {

    }

    /**
     * 수정 데이터 요청
     *
     * @param
     * imageNo(PathVariable)
     * , principal
     *
     * @return
     * ResponseEntity<ResponseDetailAndModifyDTO<Any>>
     */
    @GetMapping("/patch-detail/{imageNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getPatchDetail(@PathVariable imageNo: Long, principal: Principal) {

    }

    /**
     * 수정 데이터 중 이미지 요청
     *
     * @param
     * imageNo(PathVariable)
     *
     * @Return
     * ResponseEntity<List<ImageDataDTO>>
     */
    @GetMapping("/patch-detail/image/{imageNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getPatchImageData(@PathVariable imageNo: Long) {

    }

    /**
     * 수정 요청
     *
     * @param
     * imageNo(PathVariable)
     * , imageFiles
     * , deleteFiles
     * , imageTitle
     * , imageContent
     *
     * @return
     * ResponseEntity<Long>
     */
    @PatchMapping("/{imageNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun patchImageBoard(@PathVariable imageNo: Long) {

    }

    /**
     * 게시글 삭제
     *
     * @param
     * imageNo(PathVariable)
     * , principal
     *
     * @Return
     * ResponseEntity<String>
     */
    @DeleteMapping("/{imageNo}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun deleteImageBoard(@PathVariable imageNo: Long, principal: Principal){

    }

    /**
     * 파일 요청
     *
     * @param
     * imageName(PathVariable)
     *
     * @return
     * ResponseEntity<byte[]>
     */
    @GetMapping("/display/{imageName}")
    fun getFile(@PathVariable imageName: String) {

    }
}