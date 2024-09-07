package com.example.boardproject_kt_ver_default.domain.entity

import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardPostDTO
import com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard.HierarchicalBoardReplyPostDTO
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.ToString
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.util.Date

@Entity
@ToString
class HierarchicalBoard(
    @ManyToOne
    @JoinColumn(name = "userId")
    var member: Member,
    var boardTitle: String,
    var boardContent: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var boardNo: Long = 0

    var boardGroupNo: Long? = 0

    var boardUpperNo: String? = null

    var boardIndent: Int? = 0

    @CreationTimestamp
    var boardDate: LocalDate? = null

    fun setInsertionOtherData(){
        this.boardGroupNo = this.boardNo
        this.boardUpperNo = this.boardNo.toString()
        this.boardIndent = 0
    }

    fun setInsertionReplyData(dto: HierarchicalBoardReplyPostDTO) {
        this.boardGroupNo = dto.boardGroupNo
        this.boardUpperNo = dto.boardUpperNo + "," + this.boardNo.toString()
        this.boardIndent = dto.boardIndent + 1
    }

    fun setPatchData(boardDTO: HierarchicalBoardPostDTO) {
        this.boardTitle = boardDTO.boardTitle
        this.boardContent = boardDTO.boardContent
    }

}