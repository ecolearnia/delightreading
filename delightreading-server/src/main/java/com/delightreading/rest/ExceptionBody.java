package com.delightreading.rest;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class ExceptionBody {
    private final HttpStatus httpCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<ExceptionDetail> details;

    public ExceptionBody(String message, HttpStatus httpCode, List<ExceptionDetail> details) {
        this.httpCode = httpCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = Optional.ofNullable(details).orElse(Collections.emptyList());
    }
}
