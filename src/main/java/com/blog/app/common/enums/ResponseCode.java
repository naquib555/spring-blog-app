package com.blog.app.common.enums;

public enum ResponseCode {
    OPERATION_SUCCESSFUL(100, "Success"),
    OPERATION_FAILED(101, ""),
    NO_RECORD_FOUND(102, "No record Found"),
    REQUEST_PARAM_MISSING(103, ""),
    BAD_CREDENTIALS(104, "Invalid username or password"),
    AUTHENTICATION_FAIL(105, "Invalid username or password"),
    USERNAME_ALREADY_EXISTS(106, "Username already exists"),
    UNAUTHORIZED_TOKEN(107, ""),
    UNAUTHORIZED_ACCESS(108, "Not authorized"),
    INVALID_ARGUMENT(109, ""),
    TOKEN_EXPIRED(110, ""),
    SECURITY_ERROR(111, ""),
    DATABASE_ERROR(112, ""),
    VALIDATION_ERROR(112, "Validation Error"),
    RUNTIME_ERROR(500, "Unable to process the request"),
    REMOTE_ERROR(502, "");

    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
