package com.blog.app.service.impl;

import com.blog.app.common.util.Utils;
import com.blog.app.enums.RoleName;
import com.blog.app.model.entities.RoleEntity;
import com.blog.app.model.entities.UserEntity;
import com.blog.app.model.mapper.UserMapper;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.model.payload.auth.TokenRequest;
import com.blog.app.model.payload.auth.TokenResponse;
import com.blog.app.model.payload.registration.RegistrationRequest;
import com.blog.app.repository.RoleRepository;
import com.blog.app.repository.UserRepository;
import com.blog.app.security.JwtTokenProvider;
import com.blog.app.service.IAuthenticationService;
import com.blog.app.common.enums.Flag;
import com.blog.app.common.enums.ResponseCode;
import com.blog.app.common.exception.AppException;
import com.blog.app.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        tokenRequest.getUsername(),
                        tokenRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return new TokenResponse(jwt);
    }

    @Override
    public ApiResponse registerUser(RegistrationRequest registrationRequest, RoleName roleName) {
        if(userRepository.existsByUsername(registrationRequest.getUsername())) {
            return new ApiResponse (ResponseCode.USERNAME_ALREADY_EXISTS.getCode(), ResponseCode.USERNAME_ALREADY_EXISTS.getMessage());
        }

        RoleEntity role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new AppException(String.format("%s Role not set", roleName.name())));

        UserEntity userEntity = new UserEntity(registrationRequest,
                passwordEncoder.encode(registrationRequest.getPassword()),
                roleName == RoleName.ADMIN ? Integer.parseInt(Flag.ACTIVE.getFlag()) : Integer.parseInt(Flag.INACTIVE.getFlag()),
                LocalDateTime.now(),
                Collections.singleton(role));

        userRepository.save(userEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "User registered successfully");
    }

    @Override
    public ApiResponse logout(HttpServletRequest request) {
        String token = JwtTokenProvider.getJwtFromRequest(request);
        JwtTokenProvider.addBlackList(token);


        SecurityContextHolder.clearContext();

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), "Logout successfully");
    }
}
