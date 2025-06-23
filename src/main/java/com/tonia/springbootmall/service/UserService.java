package com.tonia.springbootmall.service;

import com.tonia.springbootmall.dto.UserRegisterRequest;
import com.tonia.springbootmall.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
