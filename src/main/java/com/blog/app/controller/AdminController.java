package com.blog.app.controller;

import com.blog.app.common.enums.Flag;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.security.domain.UserPrincipal;
import com.blog.app.service.IBlogService;
import com.blog.app.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private IUserManagementService userManagementService;

    @Autowired
    private IBlogService blogService;

    @GetMapping("/activeUserList")
    public ApiResponse getAllActiveUserList() {
        return userManagementService.getUserListByActiveStatus(Flag.ACTIVE);
    }

    @GetMapping("/inactiveUserList")
    public ApiResponse getAllInactiveUserList() {
        return userManagementService.getUserListByActiveStatus(Flag.INACTIVE);
    }

    @GetMapping("/activateUser/{id}")
    public ApiResponse activateUser(@PathVariable Long id) {
        return userManagementService.userActivation(id, Flag.ACTIVE);
    }

    @GetMapping("/deactivateUser/{id}")
    public ApiResponse deactivateUser(@PathVariable Long id) {
        return userManagementService.userActivation(id, Flag.INACTIVE);
    }

    @GetMapping("/approveBlog/{id}")
    public ApiResponse approveBlog(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return blogService.approveBlog(id, userPrincipal.getUserId());
    }

    @GetMapping("/deleteBlog/{id}")
    public ApiResponse deleteBlog(@PathVariable Long id, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return blogService.deletePost(id, userPrincipal);
    }
}
