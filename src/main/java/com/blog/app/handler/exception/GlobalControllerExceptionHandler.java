package com.blog.app.handler.exception;

import com.blog.app.common.exception.AppException;
import com.blog.app.common.exception.AuthenticationException;
import com.blog.app.common.exception.ResourceNotFoundException;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.common.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value={RuntimeException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse exceptionHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.RUNTIME_ERROR.getCode(), ResponseCode.RUNTIME_ERROR.getMessage());
    }

    @ExceptionHandler(value={BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse badCredentialsHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.BAD_CREDENTIALS.getCode(), ResponseCode.AUTHENTICATION_FAIL.getMessage());
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse accessDeniedExceptionHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage());
    }

    @ExceptionHandler(value={InternalAuthenticationServiceException.class, UsernameNotFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse exceptionMessageHandlingResponse(Exception exception) {
        return new ApiResponse(ResponseCode.AUTHENTICATION_FAIL.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.OK)
    ApiResponse methodArgumentNotValidExceptionMessageHandlingResponse(MethodArgumentNotValidException exception) {
        return new ApiResponse(ResponseCode.VALIDATION_ERROR.getCode(), ResponseCode.VALIDATION_ERROR.getMessage(), exception.getAllErrors());
    }
}
