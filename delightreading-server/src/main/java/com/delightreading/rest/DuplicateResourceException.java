package com.delightreading.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.util.Collections;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DuplicateResourceException extends BaseException {

    public static final String messageFormat = "Requested resource: %s  {%s : %s} already exists";

    public DuplicateResourceException(String resourceType, String lookupName, String lookupContext) {
        super(new ExceptionBody(REASON_EXISTS_ALREADY, HttpStatus.CONFLICT,
                Collections.singletonList(ExceptionDetail.builder()
                .field(resourceType)
                .actualValue(lookupName)
                .code("RESOURCE_ALREADY_EXISTS")
                .errors(Collections.singletonList(String.format(messageFormat, resourceType, lookupName, lookupContext)))
                .build())));

    }
}
