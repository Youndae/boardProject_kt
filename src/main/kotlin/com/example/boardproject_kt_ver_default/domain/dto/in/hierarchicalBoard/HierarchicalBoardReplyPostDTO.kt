package com.example.boardproject_kt_ver_default.domain.dto.`in`.hierarchicalBoard

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class HierarchicalBoardReplyPostDTO @JsonCreator constructor(
    @JsonProperty("boardTitle") val boardTitle: String,
    @JsonProperty("boardContent") val boardContent: String,
    @JsonProperty("boardGroupNo") val boardGroupNo: Long,
    @JsonProperty("boardIndent") val boardIndent: Int,
    @JsonProperty("boardUpperNo") val boardUpperNo: String
)
