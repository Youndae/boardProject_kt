package com.example.boardproject_kt.domain.entity

import com.example.boardproject_kt.domain.dto.imageBoard.request.ImageBoardRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "image_board")
class ImageBoard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val member: Member,

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    val createdAt: LocalDateTime? = null,
) {

    fun updateImageBoardData(dto: ImageBoardRequest) {
        this.title = dto.title;
        this.content = dto.content;
    }
}