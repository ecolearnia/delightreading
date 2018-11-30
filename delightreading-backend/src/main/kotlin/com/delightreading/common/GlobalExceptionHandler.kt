package com.delightreading.common

import org.springframework.dao.DataAccessException
import org.springframework.dao.NonTransientDataAccessException
import org.springframework.dao.RecoverableDataAccessException
import org.springframework.dao.TransientDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {


    @ExceptionHandler(ApiException::class)
    fun handleApiException(apiException: ApiException, request: WebRequest): ResponseEntity<*> {
        return handleExceptionInternal(
            apiException,
            request
        )
    }

    @ExceptionHandler(NonTransientDataAccessException::class)
    fun handleNonTransientDataAccessException(
        exception: NonTransientDataAccessException,
        request: WebRequest
    ): ResponseEntity<*> {
        val apiException =
            ApiException(ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, exception.message), exception)
        return handleExceptionInternal(
            apiException,
            request
        )
    }

    @ExceptionHandler(value = arrayOf(TransientDataAccessException::class, RecoverableDataAccessException::class))
    fun handleTransientDataAccessException(exception: DataAccessException, request: WebRequest): ResponseEntity<*> {
        // This is retryable
        val apiException =
            ApiException(ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, exception.message), exception)
        return handleExceptionInternal(
            apiException,
            request
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnknownException(exception: Exception, request: WebRequest): ResponseEntity<*> {
        // Unexpected Error
        val apiException =
            ApiException(ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, exception.message), exception)
        return handleExceptionInternal(
            apiException,
            request
        )
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        val exceptionItems = ex.bindingResult.fieldErrors.stream()
            .map{ err -> ExceptionItem(err.field, err.rejectedValue, err.code, listOf(err.defaultMessage)) }
            .collect(Collectors.toList())

        val apiException = ApiException(ExceptionBody(status, ex.message, exceptionItems), ex)
        return handleExceptionInternal(
            apiException,
            request
        )
    }

    fun handleExceptionInternal(
        exception: ApiException,
        request: WebRequest
    ): ResponseEntity<Any> {
        // log.error("Handling Exception", ex)
        val headers: HttpHeaders = HttpHeaders.EMPTY
        return super.handleExceptionInternal(exception, exception.exceptionBody, headers, exception.exceptionBody.httpCode ?: HttpStatus.INTERNAL_SERVER_ERROR , request)
    }
//
//    protected override fun handleExceptionInternal(
//        ex: Exception, @Nullable body: Any?,
//        headers: HttpHeaders?,
//        status: HttpStatus,
//        request: WebRequest
//    ): ResponseEntity<Any> {
//        log.error("Exception occured", ex)
//        return super.handleExceptionInternal(ex, body, headers, status, request)
//    }
}