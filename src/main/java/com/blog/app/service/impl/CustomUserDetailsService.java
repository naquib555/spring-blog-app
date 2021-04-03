package com.blog.app.service.impl;

import com.blog.app.common.enums.Flag;
import com.blog.app.common.exception.AppException;
import com.blog.app.common.exception.AuthenticationException;
import com.blog.app.model.entities.UserEntity;
import com.blog.app.repository.UserRepository;
import com.blog.app.security.domain.UserPrincipal;
import com.blog.app.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

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
}