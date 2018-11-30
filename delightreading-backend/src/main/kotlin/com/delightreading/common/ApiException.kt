package com.delightreading.common

import org.springframework.core.NestedRuntimeException

open class ApiException(val exceptionBody: ExceptionBody, override val cause: Throwable? = null)
    : NestedRuntimeException(exceptionBody.message, cause) {

}