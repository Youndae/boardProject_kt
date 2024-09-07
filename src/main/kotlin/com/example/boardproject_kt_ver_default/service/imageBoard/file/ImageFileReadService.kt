package com.example.boardproject_kt_ver_default.service.imageBoard.file

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files

@Service
class ImageFileReadService {

    @Value("\${file.board.path}")
    lateinit var filePath: String

    fun getFile(imageName: String): ResponseEntity<ByteArray> {
        val file = File(filePath + imageName)

        try{
            var header = HttpHeaders()
            header.add("Content-Type", Files.probeContentType(file.toPath()))

            return ResponseEntity(FileCopyUtils.copyToByteArray(file), HttpStatus.OK)
        }catch (e: IOException) {
            e.printStackTrace()
            return ResponseEntity(null, HttpStatus.OK)
        }
    }
}