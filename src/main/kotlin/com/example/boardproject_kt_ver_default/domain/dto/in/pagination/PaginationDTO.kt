package com.example.boardproject_kt_ver_default.domain.dto.`in`.pagination

data class PaginationDTO(
    val pageNum: Int
){
    var keyword: String? = null
    var searchType: String? = null
    val boardAmount: Int = 20
    val imageAmount: Int = 15

    constructor(pageNum: Int, keyword: String?, searchType: String?): this(
        pageNum = pageNum
    ){
        this.keyword =if(keyword == null){
                            null
                        }else {
                            "%$keyword%"
                        }
        this.searchType = searchType
    }

}
