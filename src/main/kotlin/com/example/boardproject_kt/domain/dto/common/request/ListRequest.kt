package com.example.boardproject_kt.domain.dto.common.request

data class ListRequest(
    val page: Int? = 1,
    val keyword: String? = null,
    val searchType: String? = null
) {
    fun validate() {
        if((keyword == null) xor (searchType == null))
            throw IllegalArgumentException("Invalid RequestParam")
    }
}
