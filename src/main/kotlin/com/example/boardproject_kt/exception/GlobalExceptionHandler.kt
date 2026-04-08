package com.example.boardproject_kt.exception

import com.example.boardproject_kt.domain.dto.response.ExceptionResponse
import com.example.boardproject_kt.domain.dto.response.exception.ValidationError
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val log = KotlinLogging.logger {  }

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ExceptionResponse<Unit>> {
        log.warn { "Business Error: ${e.message}" }

        return ResponseEntity.status(e.errorCode.httpStatus.value())
            .body(
                ExceptionResponse.exception(
                    errorCode = e.errorCode,
                    message = e.message
                )
            );
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        log.warn { "HandleMethodArgumentNotValidExceptionHandler::message : ${ex.message}" }
        log.warn { "HandleMethodArgumentNotValidExceptionHandler::AllErrors : ${ex.allErrors}" }

        val errors: List<ValidationError> = ex.fieldErrors
            .map { err -> ValidationError(
                field = err.field,
                constraint = err.code,
                validationMessage = err.defaultMessage
            ) }


        return ResponseEntity.badRequest()
            .body(
                ExceptionResponse.validationException(
                    errorCode = ErrorCode.BAD_REQUEST,
                    errors = errors,
                    message = ErrorCode.BAD_REQUEST.message
                )
            )
    }

    override fun handleHandlerMethodValidationException(
        ex: HandlerMethodValidationException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        log.warn { "HandlerMethodValidationExceptionHandler::message : ${ex.message}" }
        log.warn { "HandlerMethodValidationExceptionHandler::AllErrors : ${ex.allErrors}" }

        val errors: List<ValidationError> = ex.allErrors
            .filterIsInstance<FieldError>()
            .map {
                ValidationError(
                    field = it.field,
                    constraint = it.code,
                    validationMessage = it.defaultMessage
                )
            }

        if (errors.isEmpty()) {
            return ResponseEntity.status(ErrorCode.BAD_REQUEST.httpStatus.value())
                .body(
                    ExceptionResponse.exception<Unit>(
                        errorCode = ErrorCode.BAD_REQUEST,
                        message = ErrorCode.BAD_REQUEST.message
                    )
                )
        }

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.httpStatus.value())
            .body(
                ExceptionResponse.validationException(
                    errorCode = ErrorCode.BAD_REQUEST,
                    message = ErrorCode.BAD_REQUEST.message,
                    errors = errors
                )
            )
    }


    @ExceptionHandler(
        NullPointerException::class,
        CustomIOException::class
    )
    fun handleInternalServerError(e: RuntimeException): ResponseEntity<ExceptionResponse<Unit>> {
        log.warn { "Internal server error: ${e.message}" }

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.httpStatus.value())
            .body(
                ExceptionResponse.exception(
                    errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
                    message = ErrorCode.INTERNAL_SERVER_ERROR.message
                )
            )
    }
}