package com.tonia.springbootmall.service;

import com.tonia.springbootmall.dto.UserLoginRequest;
import com.tonia.springbootmall.dto.UserRegisterRequest;
import com.tonia.springbootmall.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User login(UserLoginRequest userLoginRequest);
    User getUserById(Integer userId);
}
