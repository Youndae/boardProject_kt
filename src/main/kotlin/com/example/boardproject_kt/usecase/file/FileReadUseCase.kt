package com.example.boardproject_kt.usecase.file

import com.example.boardproject_kt.service.file.FileDataService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FileReadUseCase(
    private val fileDataService: FileDataService
) {

    fun getBoardImageDisplay(imageName: String): ResponseEntity<ByteArray> =
        fileDataService.getBoardImageDisplay(imageName)

    fun getProfileImageDisplay(imageName: String): ResponseEntity<ByteArray> =
        fileDataService.getProfileImageDisplay(imageName)
}