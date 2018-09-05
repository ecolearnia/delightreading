package com.delightreading.rest;

import org.springframework.core.NestedRuntimeException;

//@Data
public class BaseException extends NestedRuntimeException {

    public static final String REASON_INVALID_PAYLOAD = "One or more fields in the request payload is invalid.";
    public static final String REASON_VALIDATION_FAIL = "Validation Failure.";
    public static final String REASON_RETRYABLE = "An unexpected error has occurred, Please retry your request later.";
    public static final String REASON_NON_RETRYABLE = "Please correct the input.";
    public static final String REASON_EXISTS_ALREADY = "Resource already exists.";
    public static final String REASON_NOT_FOUND = "Resource not found.";
    public static final String REASON_UNEXPECTED_ERROR = "Unexpected Error.";

    private final ExceptionBody exceptionBody;

    public BaseException(ExceptionBody exceptionBody) {
        super(exceptionBody.getMessage());
        this.exceptionBody = exceptionBody;
    }

    public BaseException(Throwable cause, ExceptionBody exceptionBody) {
        super(exceptionBody.getMessage(), cause);
        this.exceptionBody = exceptionBody;
    }

    public ExceptionBody getExceptionBody() {
        return exceptionBody;
    }
}
