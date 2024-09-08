package com.example.boardproject_kt_ver_default.domain.dto.`in`.imageBoard

import com.example.boardproject_kt_ver_default.domain.entity.ImageBoard
import com.example.boardproject_kt_ver_default.domain.entity.Member
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ImageBoardPostDTO @JsonCreator constructor(
    @JsonProperty("imageTitle") val imageTitle: String,
    @JsonProperty("imageContent") val imageContent: String
){
    fun toEntity(member: Member): ImageBoard {
        return ImageBoard(
            this.imageTitle,
            member,
            this.imageContent
        )
    }
}
