package com.example.boardproject_kt_ver_default.domain.entity

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

    fun setInsertionOtherData(bno: Long){
        this.boardGroupNo = bno
        this.boardUpperNo = bno.toString()
        this.boardIndent = 0
    }

}