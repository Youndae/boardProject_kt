package com.example.boardproject_kt_ver_default.domain.entity

import com.example.boardproject_kt_ver_default.domain.dto.`in`.comment.CommentReplyInsertDTO
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.util.Date

@Entity
class Comment(
    @ManyToOne
    @JoinColumn(name = "userId")
    val member: Member,
    var commentContent: String,
    var commentStatus: Int
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var commentNo: Long? = null

    @CreationTimestamp
    var commentDate: LocalDate? = null

    var commentGroupNo: Long? = null

    var commentIndent: Int? = null

    var commentUpperNo: String? = null

    @ManyToOne
    @JoinColumn(name = "imageNo")
    var imageBoard: ImageBoard? = null

    @ManyToOne
    @JoinColumn(name = "boardNo")
    var hierarchicalBoard: HierarchicalBoard? = null


    fun setCommentInsertPatchData() {
        this.commentGroupNo = this.commentNo
        this.commentIndent = 0
        this.commentUpperNo = this.commentNo.toString()
    }

    fun setCommentReplyPatchData(commentReplyDTO: CommentReplyInsertDTO) {
        this.commentGroupNo = commentReplyDTO.commentGroupNo
        this.commentIndent = commentReplyDTO.commentIndent + 1
        this.commentUpperNo = commentReplyDTO.commentUpperNo + "," + this.commentNo.toString()
    }

}