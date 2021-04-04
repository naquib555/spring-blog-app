package com.blog.app.service.impl;

import com.blog.app.common.enums.Flag;
import com.blog.app.common.enums.ResponseCode;
import com.blog.app.common.exception.AppException;
import com.blog.app.common.exception.AuthenticationException;
import com.blog.app.common.util.Utils;
import com.blog.app.enums.RoleName;
import com.blog.app.model.entities.RoleEntity;
import com.blog.app.model.entities.UserEntity;
import com.blog.app.model.mapper.UserMapper;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.repository.UserRepository;
import com.blog.app.security.domain.UserPrincipal;
import com.blog.app.common.exception.ResourceNotFoundException;
import com.blog.app.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService, IUserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, AppException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        if(userEntity.getIsActive() == Integer.parseInt(Flag.INACTIVE.getFlag())) throw new AppException("Inactive user");

        return UserPrincipal.create(userEntity);
    }


    @Transactional
    public UserDetails loadUserById(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserEntity getUserById(Long id) {
        UserEntity user = userRepository.findByUserId(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return user;
    }

    @Override
    public ApiResponse<List<UserMapper>> getUserListByActiveStatus(Flag flag) {
        RoleEntity role = new RoleEntity();
        role.setRoleName(RoleName.USER);

        UserEntity searchParameter = new UserEntity();
        searchParameter.setIsActive(Integer.parseInt(flag.getFlag()));
        searchParameter.setRoles(Collections.singleton(role));

        List<UserMapper> userList = userRepository.findAll(Example.of(searchParameter))
                //userRepository.findAllByIsActiveAndRoles(Integer.parseInt(flag.getFlag()), Collections.singleton(role))
                .stream()
                .map(UserMapper::new)
                .collect(Collectors.toList());

        ResponseCode responseCode = !userList.isEmpty() ? ResponseCode.OPERATION_SUCCESSFUL : ResponseCode.NO_RECORD_FOUND;

        return new ApiResponse(responseCode.getCode(), responseCode.getMessage(), userList);


    }

    @Override
    public ApiResponse userActivation(Long userId, Flag flag) throws UsernameNotFoundException {
        if(Utils.isEmpty(userId)) return new ApiResponse(ResponseCode.INVALID_ARGUMENT.getCode(), "UserId cannot be empty");
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + userId));

        if(userEntity.getIsActive() == Integer.parseInt(flag.getFlag()))
            return new ApiResponse(ResponseCode.OPERATION_FAILED.getCode(), String.format("User is already %s", flag.name()));

        userEntity.setIsActive(Integer.parseInt(flag.getFlag()));

        userRepository.save(userEntity);

        return new ApiResponse(ResponseCode.OPERATION_SUCCESSFUL.getCode(), ResponseCode.OPERATION_SUCCESSFUL.getMessage());
    }
}