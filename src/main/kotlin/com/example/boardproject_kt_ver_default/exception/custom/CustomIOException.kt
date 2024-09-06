package com.example.boardproject_kt_ver_default.exception.custom

import com.example.boardproject_kt_ver_default.exception.domain.ErrorCode

class CustomIOException(
    message: String
): CustomException(
    errorCode = ErrorCode.IO_EXCEPTION,
    message = message
)