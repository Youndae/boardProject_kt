package com.example.boardproject_kt.service.file

import com.example.boardproject_kt.domain.entity.ImageData
import com.example.boardproject_kt.domain.enums.SaveImageKey
import com.example.boardproject_kt.exception.CustomIOException
import com.example.boardproject_kt.exception.ErrorCode
import io.github.oshai.kotlinlogging.KotlinLogging
import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.imageio.ImageIO

private val log = KotlinLogging.logger {  }

@Service
class FileDataService(
    @Value("#{filePath['file.board.path']}")
    private val boardPath: String,
    @Value("#{filePath['file.profile.path']}")
    private val profilePath: String
) {

    companion object {
        private const val MAX_PIXEL = 5000
        private const val SIZE_300 = 300
        private const val SIZE_600 = 600
        private const val EXTENSION = "jpg"
    }

    fun profileImageSave(file: MultipartFile): String {
        validationResolution(file)
        val saveNamePrefix = createSaveFileName(file)[SaveImageKey.SAVE_NAME]!!
        val originalImage = ImageIO.read(file.inputStream)

        return imageResizing(originalImage, saveNamePrefix, SIZE_300, profilePath)
    }

    fun boardImageInsert(images: List<MultipartFile>, step: Int): List<ImageData> {
        val saveImageData = mutableListOf<ImageData>()

        var imageStep = step

        for(image in images) {
            try {
                val map = boardImageSave(image)
                saveImageData.add(
                    ImageData(
                        imageName = map[SaveImageKey.SAVE_NAME]!!,
                        originName = map[SaveImageKey.ORIGIN_NAME]!!,
                        imageStep = imageStep++
                    )
                )
            }catch(e: Exception) {
                log.error { "File Transfer Error. Rollback save file" }
                saveImageData.forEach { cleanupImageBoardFiles(it.imageName) }

                log.error { "FileDataService.boardImageInsert :: Failed transfer file" }

                throw CustomIOException(ErrorCode.INTERNAL_SERVER_ERROR)
            }
        }

        return saveImageData
    }


    private fun boardImageSave(file: MultipartFile): Map<SaveImageKey, String> {
        validationResolution(file)
        val saveImageMap = createSaveFileName(file)
        val saveName = saveImageMap[SaveImageKey.SAVE_NAME]!!

        return try {
            val originalImage = ImageIO.read(file.inputStream)

            Thumbnails.of(originalImage)
                .scale(1.0)
                .outputFormat(EXTENSION)
                .outputQuality(0.9)
                .toFile(File(boardPath + saveName))

            imageResizing(originalImage, saveName, SIZE_300, boardPath)
            imageResizing(originalImage, saveName, SIZE_600, boardPath)

            saveImageMap
        }catch (e: Exception) {
            if(saveImageMap.isNotEmpty())
                cleanupImageBoardFiles(saveName)

            throw CustomIOException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    private fun imageResizing(
        file: BufferedImage,
        fileName: String,
        size: Int,
        filePath: String
    ) : String {
        val saveName = createResizeName(fileName, size)

        val targetFile = File(filePath + saveName)

        Thumbnails.of(file)
            .size(size, size)
            .outputFormat(EXTENSION)
            .outputQuality(0.8)
            .toFile(targetFile)

        return saveName
    }

    private fun cleanupImageBoardFiles(saveName: String) {
        deleteFile(boardPath, saveName)
        val resized300Name = createResizeName(saveName, SIZE_300)
        val resized600Name = createResizeName(saveName, SIZE_600)
        deleteFile(boardPath, resized300Name)
        deleteFile(boardPath, resized600Name)
    }

    private fun createResizeName(fileName: String, size: Int): String {
        val saveNamePrefix = fileName.substring(0, fileName.lastIndexOf('.'))

        return "${saveNamePrefix}_$size.$EXTENSION"
    }

    private fun validationResolution(file: MultipartFile) {
        ImageIO.createImageInputStream(file.inputStream).use { iis ->
            val readers = ImageIO.getImageReaders(iis)

            if(readers.hasNext()){
                val reader = readers.next()

                try {
                    reader.input = iis
                    val width = reader.getWidth(0)
                    val height = reader.getHeight(0)

                    if(width > MAX_PIXEL || height > MAX_PIXEL)
                        throw IllegalArgumentException("이미지 해상도 너무 높음")
                } finally {
                    reader.dispose()
                }
            }else {
                throw IllegalArgumentException("지원하지 않는 이미지 형식")
            }
        }
    }

    private fun createSaveFileName(image: MultipartFile): Map<SaveImageKey, String> {
        val originalName = image.originalFilename ?: "unknown"
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val saveName = "$timestamp${UUID.randomUUID()}.$EXTENSION"

        return mapOf(
            SaveImageKey.SAVE_NAME to saveName,
            SaveImageKey.ORIGIN_NAME to originalName
        )
    }

    fun deleteFile(filePath: String, image: String) {
        val file = File(filePath + image)

        if(file.exists())
            file.delete()
    }

    fun getProfileImageDisplay(imageName: String): ResponseEntity<ByteArray> {
        return getDisplayImage(profilePath, imageName)
    }

    fun getBoardImageDisplay(imageName: String): ResponseEntity<ByteArray> {
        return getDisplayImage(boardPath, imageName)
    }

    private fun getDisplayImage(filePath: String, imageName: String): ResponseEntity<ByteArray> {
        val file = File(filePath + imageName)

        return try {
            val header = HttpHeaders().apply {
                add("Content-Type", "image/jpeg")
            }

            val imageBytes = FileCopyUtils.copyToByteArray(file)

            ResponseEntity(imageBytes, header, HttpStatus.OK)
        }catch(e: IOException) {
            e.printStackTrace()
            throw CustomIOException(ErrorCode.BAD_REQUEST)
        }
    }

    fun deleteImageBoardFile(deleteFiles: List<String>) {
        deleteFiles.forEach { cleanupImageBoardFiles(it) }
    }
}