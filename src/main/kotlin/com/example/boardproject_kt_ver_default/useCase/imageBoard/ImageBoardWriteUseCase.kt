package com.example.boardproject_kt_ver_default.useCase.imageBoard

import com.example.boardproject_kt_ver_default.domain.dto.business.imageBoard.FileDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.imageBoard.ImageBoardPostDTO
import com.example.boardproject_kt_ver_default.service.auth.PrincipalReadService
import com.example.boardproject_kt_ver_default.service.imageBoard.ImageBoardWriteService
import com.example.boardproject_kt_ver_default.service.imageBoard.file.ImageFileWriteService
import com.example.boardproject_kt_ver_default.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class ImageBoardWriteUseCase(
    private val imageBoardWriteService: ImageBoardWriteService,
    private val principalReadService: PrincipalReadService,
    private val fileWriteService: ImageFileWriteService
) {

    @Value("\${file.board.path}")
    lateinit var boardFilePath: String

    private val log = logger()

    @Transactional(rollbackFor = [Exception::class])
    fun postBoard(dto: ImageBoardPostDTO, files: List<MultipartFile>, principal: Principal): Long {
        var fileNameList: List<FileDTO>? = null
        try{
            fileNameList = files.map { fileWriteService.saveFile(boardFilePath, it) }.toList()
            val member = principalReadService.getMemberEntity(principal)
            val imageBoard = dto.toEntity(member)
            return imageBoardWriteService.postBoard(imageBoard, fileNameList)
        }catch (e: Exception) {
            log.error("postBoard Exception. ${e.message}")

            fileNameList?.forEach{ fileWriteService.deleteFile(boardFilePath, it.imageName) }

            throw IllegalArgumentException("postBoard Exception")
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun patchBoard(imageNo: Long,
                    dto: ImageBoardPostDTO,
                    files: List<MultipartFile>?,
                    deleteFiles: List<String>?,
                    principal: Principal
                ): Long {
        var fileNameList: List<FileDTO>? = null

        try{
            fileNameList = files?.map { fileWriteService.saveFile(boardFilePath, it) }!!.toList()
            val member = principalReadService.getMemberEntity(principal)
            val result = imageBoardWriteService.patchBoard(dto, member, fileNameList, deleteFiles, imageNo)
            deleteFiles?.forEach { fileWriteService.deleteFile(boardFilePath, it) }
            return result
        }catch (e: Exception) {
            log.error("patchBoard Exception. ${e.message}")

            fileNameList?.forEach{ fileWriteService.deleteFile(boardFilePath, it.imageName) }

            throw IllegalArgumentException("postBoard Exception")
        }
    }

    fun deleteBoard(imageNo: Long, principal: Principal): String {
        return imageBoardWriteService.deleteBoard(imageNo, principal)
    }
}