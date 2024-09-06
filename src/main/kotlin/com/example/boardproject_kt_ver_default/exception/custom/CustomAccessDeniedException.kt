package com.example.boardproject_kt_ver_default.exception.custom

import com.example.boardproject_kt_ver_default.exception.domain.ErrorCode

class CustomAccessDeniedException(
    message: String
): CustomException(
    errorCode = ErrorCode.ACCESS_DENIED,
    message = message
)