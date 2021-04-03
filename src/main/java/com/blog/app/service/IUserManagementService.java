package com.blog.app.service;

import com.blog.app.common.enums.Flag;
import com.blog.app.model.mapper.UserMapper;
import com.blog.app.model.payload.ApiResponse;

import java.util.List;

public interface IUserManagementService {

    ApiResponse<List<UserMapper>> getUserListByActiveStatus(Flag flag);
    ApiResponse userActivation(Long userId, Flag flag);

}
