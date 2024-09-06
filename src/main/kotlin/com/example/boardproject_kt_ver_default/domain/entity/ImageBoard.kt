package com.example.boardproject_kt_ver_default.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.util.Date

@Entity
class ImageBoard(
    var imageTitle: String,
    @ManyToOne
    @JoinColumn(name = "userId")
    var member: Member,
    var imageContent: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var imageNo: Long = 0

    @CreationTimestamp
    var imageDate: LocalDate? =null

    @OneToMany(mappedBy = "imageBoard", cascade = [CascadeType.ALL])
    private var imageDataSet: MutableList<ImageData> = mutableListOf()
    val imageData: List<ImageData>
        get() = imageDataSet.toList()

    fun addImageData(imageData: ImageData) {
        imageDataSet += imageData
        imageData.imageBoardSet(this)
    }
}