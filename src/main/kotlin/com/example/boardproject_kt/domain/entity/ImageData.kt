package com.example.boardproject_kt.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "image_data")
class ImageData(
    @Id
    @Column(name = "image_name")
    val imageName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    var imageBoard: ImageBoard? = null,

    @Column(name = "origin_name", nullable = false)
    val originName: String,

    @Column(name = "image_step", nullable = false)
    val imageStep: Int
) {

    fun updateImageBoard(imageBoard: ImageBoard) {
        this.imageBoard = imageBoard
    }
}