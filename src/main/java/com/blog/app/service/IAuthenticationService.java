package com.blog.app.service;

import com.blog.app.enums.RoleName;
import com.blog.app.model.mapper.UserMapper;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.model.payload.auth.TokenRequest;
import com.blog.app.model.payload.auth.TokenResponse;
import com.blog.app.model.payload.registration.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

    TokenResponse generateToken(TokenRequest tokenRequest);
    ApiResponse<UserMapper> registerUser(RegistrationRequest registrationRequest, RoleName roleName);
    ApiResponse logout(HttpServletRequest request);
}
