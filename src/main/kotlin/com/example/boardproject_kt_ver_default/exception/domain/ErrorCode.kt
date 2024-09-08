package com.example.boardproject_kt_ver_default.exception.domain

enum class ErrorCode(val httpStatus: Int, val message: String) {

    TOKEN_STEALING(800, "TokenStealingException")
    , ACCESS_DENIED(403, "AccessDeniedException")
    , IO_EXCEPTION(400, "IOException");

}