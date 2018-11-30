package com.delightreading.common

import org.springframework.http.HttpStatus
import java.time.Instant

data class ExceptionBody(
    val httpCode: HttpStatus? = null,
    val message: String? = null,
    val details: List<ExceptionItem>? = null,
    val timestamp: Instant = Instant.now()
) {
}