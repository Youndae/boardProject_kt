package com.example.boardproject_kt_ver_default.exception.handler

import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.boardproject_kt_ver_default.exception.custom.*
import com.example.boardproject_kt_ver_default.exception.domain.ExceptionEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.FileNotFoundException
import java.io.IOException

@RestControllerAdvice
class ExceptionHandler(): ResponseEntityExceptionHandler() {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(CustomTokenStealingException::class)
    fun tokenStealingExceptionHandler(e: CustomTokenStealingException): ResponseEntity<ExceptionEntity> {
        exceptionLog(e)

        return ResponseEntity.status(e.errorCode.httpStatus)
            .body(createExceptionEntity(e))
    }

    @ExceptionHandler(NullPointerException::class)
    fun nullPointerExceptionHandler(e: Exception): ResponseEntity<Any> {
        exceptionLog(e)

        return ResponseEntity.status(500).build()
    }

    @ExceptionHandler(AccessDeniedException::class, CustomAccessDeniedException::class)
    fun accessDeniedExceptionHandler(e: CustomAccessDeniedException): ResponseEntity<ExceptionEntity> {
        exceptionLog(e)

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(createExceptionEntity(e))
    }

    @ExceptionHandler(FileNotFoundException::class, NoSuchFileException::class)
    fun fileNotFoundExceptionHandler(e: Exception): ResponseEntity<Any> {
        exceptionLog(e)

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalAccessException::class)
    fun illegalExceptionHandler(e: Exception): ResponseEntity<Any> {
        exceptionLog(e)

        return ResponseEntity.status(HttpStatusCode.valueOf(400)).build()
    }

    @ExceptionHandler(CustomIOException::class, IOException::class)
    fun ioExceptionHandler(e: CustomIOException): ResponseEntity<ExceptionEntity> {
        exceptionLog(e)

        return ResponseEntity.status(HttpStatus.valueOf(400))
            .body(createExceptionEntity(e))
    }

    @ExceptionHandler(CustomBadCredentialsException::class, BadCredentialsException::class)
    fun badCredentialsException(e: CustomBadCredentialsException): ResponseEntity<ExceptionEntity> {
        exceptionLog(e)

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(createExceptionEntity(e))
    }

    fun createExceptionEntity(e: CustomException): ExceptionEntity {
        return ExceptionEntity(e.errorCode.httpStatus.toString(), e.errorCode.message)
    }

    fun exceptionLog(e: Exception) {
        //log 처리
        log.error(e.toString())

        var trace = e.stackTrace

        for(a: StackTraceElement in trace)
            log.error(a.toString())
    }
}