package com.example.boardproject_kt_ver_default.domain.dto.out.hierarchialBoard

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class HierarchicalBoardPatchDTO @JsonCreator constructor(
    @JsonProperty("boardNo") val boardNo: Long,
    @JsonProperty("boardTitle") val boardTitle: String,
    @JsonProperty("boardContent") val boardContent: String
)
