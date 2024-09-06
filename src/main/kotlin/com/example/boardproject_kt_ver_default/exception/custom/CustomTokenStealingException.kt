package com.example.boardproject_kt_ver_default.exception.custom

import com.example.boardproject_kt_ver_default.exception.domain.ErrorCode

class CustomTokenStealingException(
    message: String
): CustomException(
    errorCode = ErrorCode.TOKEN_STEALING,
    message = message
)