package com.example.boardproject_kt.controller

import com.example.boardproject_kt.domain.dto.common.request.ListRequest
import com.example.boardproject_kt.domain.dto.imageBoard.request.ImageBoardRequest
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardDetailResponse
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardListResponse
import com.example.boardproject_kt.domain.dto.imageBoard.response.ImageBoardPatchDetailResponse
import com.example.boardproject_kt.domain.dto.response.ApiResponse
import com.example.boardproject_kt.domain.dto.response.PageResponse
import com.example.boardproject_kt.usecase.file.FileReadUseCase
import com.example.boardproject_kt.usecase.imageBoard.ImageBoardReadUseCase
import com.example.boardproject_kt.usecase.imageBoard.ImageBoardWriteUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
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
@RequestMapping("/api/image-board")
class ImageBoardController(
    private val imageBoardReadUseCase: ImageBoardReadUseCase,
    private val imageBoardWriteUseCase: ImageBoardWriteUseCase,
    private val fileReadUseCase: FileReadUseCase
) {

    @GetMapping("")
    fun getList(request: ListRequest): ResponseEntity<ApiResponse<PageResponse<ImageBoardListResponse>>> {
        request.validate()
        val body = imageBoardReadUseCase.getImageBoardList(request)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @GetMapping("/{id}")
    fun getDetail(
        @PathVariable(name =  "id")
        @Min(value = 1)
        id: Long
    ): ResponseEntity<ApiResponse<ImageBoardDetailResponse>> {
        val body = imageBoardReadUseCase.getImageBoardDetail(id)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @PostMapping(value = [""], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun postBoard(
        @RequestParam files: List<MultipartFile>,
        @ModelAttribute @Valid request: ImageBoardRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Long>> {
        val body = imageBoardWriteUseCase.postBoard(files, request, principal.name)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(body))
    }

    @GetMapping("/patch/detail/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getPatchDetail(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        principal: Principal
    ): ResponseEntity<ApiResponse<ImageBoardPatchDetailResponse>> {
        val body = imageBoardReadUseCase.getPatchData(id, principal.name)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @PatchMapping(value = ["/{id}"], consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun patchBoard(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        @RequestParam(value = "files", required = false) files: List<MultipartFile>?,
        @RequestParam(value = "deleteFiles", required = false) deleteFiles: List<String>?,
        @ModelAttribute @Valid request: ImageBoardRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Long>> {
        val body = imageBoardWriteUseCase.patchBoard(files, deleteFiles, id, request, principal.name)

        return ResponseEntity.ok(ApiResponse.success(body))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun imageBoardDelete(
        @PathVariable(name = "id")
        @Min(value = 1)
        id: Long,
        principal: Principal
    ): ResponseEntity<Unit> {
        imageBoardWriteUseCase.deleteBoard(id, principal.name)

        return ResponseEntity.noContent().build()
    }

    @GetMapping("/display/{imageName}")
    fun getFile(
        @PathVariable(name = "imageName") imageName: String
    ): ResponseEntity<ByteArray> {

        return fileReadUseCase.getBoardImageDisplay(imageName)
    }
}