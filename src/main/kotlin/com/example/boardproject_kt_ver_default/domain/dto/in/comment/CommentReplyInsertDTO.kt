package com.example.boardproject_kt_ver_default.domain.dto.`in`.comment

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class CommentReplyInsertDTO @JsonCreator constructor(
    override val commentContent: String,
    @JsonProperty("commentGroupNo") val commentGroupNo: Long,
    @JsonProperty("commentIndent") val commentIndent: Int,
    @JsonProperty("commentUpperNo") val commentUpperNo: String
): CommentInsertDTO(commentContent) {
}