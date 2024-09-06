package com.example.boardproject_kt_ver_default.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ImageData(
    @Id
    var imageName: String,
    var oldName: String,
    var imageStep: Int
) {
    @ManyToOne
    @JoinColumn(name = "imageNo")
    lateinit var imageBoard: ImageBoard

    fun imageBoardSet(imageBoard: ImageBoard){
        this.imageBoard = imageBoard
    }
}