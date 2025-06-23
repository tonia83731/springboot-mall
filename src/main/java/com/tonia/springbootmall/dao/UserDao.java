package com.tonia.springbootmall.dao;

import com.tonia.springbootmall.dto.UserRegisterRequest;
import com.tonia.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
