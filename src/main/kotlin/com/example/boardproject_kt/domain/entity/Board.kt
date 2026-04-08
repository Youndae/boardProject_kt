package com.example.boardproject_kt.domain.entity

import com.example.boardproject_kt.domain.dto.board.request.BoardRequest
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
@Table(name = "board")
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val member: Member,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    val createdAt: LocalDateTime? = null,

    @Column(name = "group_no")
    var groupNo: Long? = null,

    val indent: Int,

    @Column(name = "upper_no")
    var upperNo: String? = null,
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

    fun updatePatchData(dto: BoardRequest) {
        this.title = dto.title
    }
}