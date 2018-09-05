package com.delightreading.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.delightreading.rest.BaseException.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity handleBaseException(BaseException baseException, WebRequest request) {
        return handleExceptionInternal(baseException, baseException.getExceptionBody(), null, baseException.getExceptionBody().getHttpCode(), request);
    }

    @ExceptionHandler(value = NonTransientDataAccessException.class)
    public ResponseEntity handleNonTransientDataAccessException(NonTransientDataAccessException exception, WebRequest request) {
        List<ExceptionDetail> exceptionDetails = Collections.singletonList(ExceptionDetail.builder()
                .code("REASON_NON_RETRYABLE")
                .errors(Collections.singletonList(exception.getMessage())).build());
        BaseException baseException = new BaseException(exception, new ExceptionBody(REASON_NON_RETRYABLE, HttpStatus.BAD_REQUEST, exceptionDetails));
        return handleExceptionInternal(baseException, baseException.getExceptionBody(), null, baseException.getExceptionBody().getHttpCode(), request);
    }

    @ExceptionHandler(value = {TransientDataAccessException.class, RecoverableDataAccessException.class})
    public ResponseEntity handleTransientDataAccessException(DataAccessException exception, WebRequest request) {
        List<ExceptionDetail> exceptionDetails = Collections.singletonList(ExceptionDetail.builder().errors(Collections.singletonList(exception.getMessage())).build());
        BaseException baseException = new BaseException(exception, new ExceptionBody(REASON_RETRYABLE, HttpStatus.INTERNAL_SERVER_ERROR, exceptionDetails));
        return handleExceptionInternal(baseException, baseException.getExceptionBody(), null, baseException.getExceptionBody().getHttpCode(), request);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleUnknownException(Exception exception, WebRequest request) {
        List<ExceptionDetail> exceptionDetails = Collections.singletonList(ExceptionDetail.builder()
                .code("REASON_UNEXPECTED_ERROR")
                .errors(Collections.singletonList(exception.getMessage())).build());
        BaseException baseException = new BaseException(exception, new ExceptionBody(REASON_UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, exceptionDetails));
        return handleExceptionInternal(baseException, baseException.getExceptionBody(), null, baseException.getExceptionBody().getHttpCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<ExceptionDetail> exceptionDetails = ex.getBindingResult().getFieldErrors().stream().map(
                err -> new ExceptionDetail(err.getField(), err.getRejectedValue(), err.getCode(), Collections.singletonList(err.getDefaultMessage()))
        ).collect(Collectors.toList());

        BaseException baseException = new BaseException(ex, new ExceptionBody(REASON_VALIDATION_FAIL, status, exceptionDetails));
        return handleExceptionInternal(baseException, baseException.getExceptionBody(), headers, baseException.getExceptionBody().getHttpCode(), request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception occured", ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
