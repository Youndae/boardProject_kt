package com.example.boardproject_kt.domain.dto.common.business

import com.example.boardproject_kt.domain.dto.common.request.ListRequest

data class PageCondition(
    val keyword: String? = null,
    val searchType: String? = null,
) {

    companion object {
        private fun formatKeyword(keyword: String?): String? = if(keyword == null ) null else "%$keyword%"

        fun from(request: ListRequest): PageCondition {
            return PageCondition(
                keyword = formatKeyword(request.keyword),
                searchType = request.searchType
            )
        }
    }
}
