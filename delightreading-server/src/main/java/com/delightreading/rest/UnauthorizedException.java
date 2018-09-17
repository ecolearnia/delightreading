package com.delightreading.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.util.Collections;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnauthorizedException extends BaseException {

    public static final String ERROR_MESSAGE = "Not authorized to access the resource";

    public UnauthorizedException(String resourceType, Number resourceId) {
        super(new ExceptionBody(ERROR_MESSAGE, HttpStatus.UNAUTHORIZED,
                Collections.singletonList(ExceptionDetail.builder()
                        .field(resourceType)
                        .actualValue(resourceId)
                        .code(HttpStatus.UNAUTHORIZED.toString())
                        .errors(Collections.singletonList(ERROR_MESSAGE))
                        .build())));
    }

    public UnauthorizedException(String resourceType, String resourceId) {
        super(new ExceptionBody(ERROR_MESSAGE, HttpStatus.UNAUTHORIZED,
                Collections.singletonList(ExceptionDetail.builder()
                        .field(resourceType)
                        .actualValue(resourceId)
                        .code(HttpStatus.UNAUTHORIZED.toString())
                        .errors(Collections.singletonList(ERROR_MESSAGE))
                        .build())));
    }
}
