package com.blog.app.controller;

import com.blog.app.common.enums.Flag;
import com.blog.app.enums.RoleName;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.model.payload.auth.TokenRequest;
import com.blog.app.model.payload.auth.TokenResponse;
import com.blog.app.model.payload.registration.RegistrationRequest;
import com.blog.app.service.IAuthenticationService;
import com.blog.app.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/tokenRequest")
    public TokenResponse tokenRequest(@Valid @RequestBody TokenRequest tokenRequest) {
        return authenticationService.generateToken(tokenRequest);
    }

    @PostMapping("/userRegistration")
    public ApiResponse userRegistration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authenticationService.registerUser(registrationRequest, RoleName.USER);
    }

    @PostMapping("/adminRegistration")
    public ApiResponse adminRegistration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authenticationService.registerUser(registrationRequest, RoleName.ADMIN);
    }
}
