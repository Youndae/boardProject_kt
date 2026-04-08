package com.example.boardproject_kt.domain.entity

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
import org.hibernate.annotations.SQLDelete
import java.time.LocalDateTime

@Entity
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val member: Member,

    @Column(columnDefinition = "TEXT", nullable = false)
    val content: String,

    @Column(name = "group_no")
    var groupNo: Long? = null,

    val indent: Int,

    @Column(name = "upper_no")
    var upperNo: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_board_id")
    val imageBoard: ImageBoard? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    val board: Board? = null,

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    val createdAt: LocalDateTime? = null,

    @Column(name = "deleted_at", columnDefinition = "DATETIME(3)")
    val deletedAt: LocalDateTime? = null
) {

    fun initializeRootPath() {
        this.groupNo = this.id
        updateUpperNo(this.id.toString())
    }

    fun initializeReplyPath(targetUpperNo: String) {
        val saveValue = "$targetUpperNo,${this.id}"
        updateUpperNo(saveValue)
    }

    private fun updateUpperNo(upperNo: String) {
        this.upperNo = upperNo
    }
}