package com.example.boardproject_kt_ver_default.service.imageBoard.file

import com.example.boardproject_kt_ver_default.domain.dto.business.imageBoard.FileDTO
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.UUID
import kotlin.jvm.Throws

@Service
class ImageFileWriteService {

    @Throws(IOException::class)
    fun saveFile(filePath: String, image: MultipartFile): FileDTO {
        val originalName: String = image.originalFilename!!
        val saveName:String = SimpleDateFormat("yyyyMMddHHmmss")
                            .format(System.currentTimeMillis()) +
                        UUID.randomUUID() +
                        originalName.substring(originalName.lastIndexOf("."))

        val saveFile = filePath + saveName

        image.transferTo(File(saveFile))

        return FileDTO(
            imageName = saveName,
            oldName = originalName
        )
    }

    @Throws(IOException::class)
    fun deleteFile(filePath: String, imageName: String) {
        val file = File(filePath + imageName)

        if(file.exists())
            file.delete()
    }
}