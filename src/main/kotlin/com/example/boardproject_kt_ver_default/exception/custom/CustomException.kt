package com.example.boardproject_kt_ver_default.exception.custom

import com.example.boardproject_kt_ver_default.exception.domain.ErrorCode

open class CustomException(
    open val errorCode: ErrorCode,
    override val message: String
): RuntimeException(message)