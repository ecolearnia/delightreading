package com.delightreading.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.HttpStatus

@JsonIgnoreProperties(ignoreUnknown = true)
class UnauthorizedException (resourceType: String, resourceId: String)
    : ApiException(
    ExceptionBody(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE,
        listOf(ExceptionItem("$resourceType:$resourceId", HttpStatus.UNAUTHORIZED.toString())))
) {
    companion object {
        val ERROR_MESSAGE = "Not authorized to access the resource"
    }
}