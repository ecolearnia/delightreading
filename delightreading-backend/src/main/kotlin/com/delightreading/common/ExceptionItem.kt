package com.delightreading.common

import com.fasterxml.jackson.annotation.JsonInclude

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

data class ExceptionItem(

    @JsonInclude(NON_NULL)
    private val element: String? = null, // E.g. the field

    @JsonInclude(NON_NULL)
    private val actualValue: Any? = null, // E.g. the value

    @JsonInclude(NON_NULL)
    private val code: String? = null, // E.g. VALIDATION

    @JsonInclude(NON_EMPTY)
    private val errors: List<String?>? = null // E.g. ["Cannot exceed 30 chars", "Only alphanumeric allowed"]
) {
}