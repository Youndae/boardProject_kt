package com.example.boardproject_kt_ver_default.controller

import com.example.boardproject_kt_ver_default.domain.dto.`in`.imageBoard.ImageBoardPostDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardListDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageBoardPatchDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.imageBoard.ImageDataDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.response.ResponseDetailDTO
import com.example.boardproject_kt_ver_default.domain.dto.out.response.ResponsePageableListDTO
import com.example.boardproject_kt_ver_default.domain.factory.ResponseFactory
import com.example.boardproject_kt_ver_default.useCase.imageBoard.ImageBoardReadUseCase
import com.example.boardproject_kt_ver_default.useCase.imageBoard.ImageBoardWriteUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/image-board")
class ImageBoardController(
    private val imageBoardReadUseCase: ImageBoardReadUseCase,
    private val imageBoardWriteUseCase: ImageBoardWriteUseCase,
    private val responseFactory: ResponseFactory
) {

    /**
     * 리스트 조회
     *
     * @param
     * pageNum, keyword, searchType
     * principal
     *
     * @return
     * ResponseEntity<ResponsePageableDTO<Any>>
     *
     */
    @GetMapping("/")
    fun getList(@RequestParam(value = "pageNum") pageNum: Int
                , @RequestParam(value = "keyword", required = false) keyword: String?
                , @RequestParam(value = "searchType", required = false) searchType: String?
                , principal: Principal?): ResponseEntity<ResponsePageableListDTO<ImageBoardListDTO>> {
        val result = imageBoardReadUseCase.getList(pageNum, keyword, searchType)

        return responseFactory.createPaginationList(result, principal)
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
    fun getDetail(@PathVariable imageNo: Long, principal: Principal?): ResponseEntity<ResponseDetailDTO<ImageBoardDetailDTO>> {
        val result = imageBoardReadUseCase.getDetail(imageNo)

        return responseFactory.createDetailResponse(result, principal)
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
    fun postBoard(@ModelAttribute dto: ImageBoardPostDTO
                , @RequestParam files: List<MultipartFile>
                , principal: Principal): ResponseEntity<Long> {

        val result = imageBoardWriteUseCase.postBoard(dto, files, principal)

        return responseFactory.createLongResponse(result)
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
    fun getPatchDetail(@PathVariable imageNo: Long, principal: Principal): ResponseEntity<ResponseDetailDTO<ImageBoardPatchDTO>> {
        val result = imageBoardReadUseCase.getPatchDetail(imageNo, principal)

        return responseFactory.createDetailResponse(result, principal)
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
    fun getPatchImageData(@PathVariable imageNo: Long): ResponseEntity<List<ImageDataDTO>> {

        return ResponseEntity(imageBoardReadUseCase.getPatchImage(imageNo), HttpStatus.OK)
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
    fun patchImageBoard(@PathVariable imageNo: Long
                        , @ModelAttribute dto: ImageBoardPostDTO
                        , @RequestParam(value = "files", required = false) files: List<MultipartFile>?
                        , @RequestParam(value = "deleteFiles", required = false) deleteFiles: List<String>?
                        , principal: Principal): ResponseEntity<Long> {

        val result = imageBoardWriteUseCase.patchBoard(imageNo, dto, files, deleteFiles, principal)

        return responseFactory.createLongResponse(result)
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
    fun deleteImageBoard(@PathVariable imageNo: Long, principal: Principal): ResponseEntity<String>{

        val result = imageBoardWriteUseCase.deleteBoard(imageNo, principal)

        return responseFactory.createStringResponse(result)
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
    fun getFile(@PathVariable imageName: String): ResponseEntity<ByteArray> {

        return imageBoardReadUseCase.getFile(imageName)
    }
}