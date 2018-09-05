package com.delightreading.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Getter
public class ExceptionDetail {

    @JsonInclude(NON_NULL)
    private String field;

    @JsonInclude(NON_NULL)
    private Object actualValue;

    @JsonInclude(NON_NULL)
    private String code;

    @JsonInclude(NON_EMPTY)
    private List<String> errors;
}
