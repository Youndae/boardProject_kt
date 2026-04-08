package com.example.boardproject_kt.domain.dto.response.exception

data class ValidationError(
    val field: String?,
    val constraint: String?,
    val validationMessage: String?
)
