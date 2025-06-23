package com.tonia.springbootmall.service.impl;

import com.tonia.springbootmall.dao.UserDao;
import com.tonia.springbootmall.dto.UserRegisterRequest;
import com.tonia.springbootmall.model.User;
import com.tonia.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest){
        Integer userId = userDao.createUser(userRegisterRequest);
        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        User user = userDao.getUserById(userId);
        return user;
    }
}
