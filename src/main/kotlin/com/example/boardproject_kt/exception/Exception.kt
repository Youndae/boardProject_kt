package com.example.boardproject_kt.exception

open class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message)

class CustomAccessDeniedException(
    errorCode: ErrorCode = ErrorCode.FORBIDDEN
) : BusinessException(errorCode)

class CustomAuthenticationException(
    errorCode: ErrorCode = ErrorCode.UNAUTHORIZED
) : BusinessException(errorCode)

class CustomInvalidJoinPolicyException(
    errorCode: ErrorCode = ErrorCode.BAD_REQUEST,
    message: String
) : BusinessException(errorCode, message)

class CustomIOException(
    errorCode: ErrorCode = ErrorCode.INTERNAL_SERVER_ERROR
) : BusinessException(errorCode)

class CustomNotFoundException(
    errorCode: ErrorCode = ErrorCode.BAD_REQUEST,
    message: String = ErrorCode.BAD_REQUEST.message
) : BusinessException(errorCode, message)