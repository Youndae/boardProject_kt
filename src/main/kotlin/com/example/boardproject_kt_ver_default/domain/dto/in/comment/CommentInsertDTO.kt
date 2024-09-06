package com.example.boardproject_kt_ver_default.domain.dto.`in`.comment

import com.example.boardproject_kt_ver_default.domain.entity.Comment
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

open class CommentInsertDTO @JsonCreator constructor(
    @JsonProperty("commentContent") open val commentContent: String
) {

    open fun toEntity(member: Member): Comment {
        return Comment(member, this.commentContent, 0)
    }
}