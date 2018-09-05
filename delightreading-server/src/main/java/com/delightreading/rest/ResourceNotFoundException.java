package com.delightreading.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.util.Collections;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceNotFoundException extends BaseException {

    public static final String messageFormat = "Requested %s with id %s not found";

    public ResourceNotFoundException(String resourceType, Number resourceId) {
        super(new ExceptionBody(REASON_NOT_FOUND, HttpStatus.NOT_FOUND,
                Collections.singletonList(ExceptionDetail.builder()
                        .field(resourceType)
                        .actualValue(resourceId)
                        .code("RESOURCE_NOT_FOUND")
                        .errors(Collections.singletonList(String.format(messageFormat, resourceType, resourceId)))
                        .build())));
    }

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(new ExceptionBody(REASON_NOT_FOUND, HttpStatus.NOT_FOUND,
                Collections.singletonList(ExceptionDetail.builder()
                        .field(resourceType)
                        .actualValue(resourceId)
                        .code("RESOURCE_NOT_FOUND")
                        .errors(Collections.singletonList(String.format(messageFormat, resourceType, resourceId)))
                        .build())));
    }
}
