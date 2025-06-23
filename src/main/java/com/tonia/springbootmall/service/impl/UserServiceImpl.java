package com.tonia.springbootmall.service.impl;

import com.tonia.springbootmall.dao.UserDao;
import com.tonia.springbootmall.dto.UserLoginRequest;
import com.tonia.springbootmall.dto.UserRegisterRequest;
import com.tonia.springbootmall.model.User;
import com.tonia.springbootmall.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


@Component
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest){
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if (user != null){
            log.warn("User {} already exists!", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exists!");
        }

        Integer userId = userDao.createUser(userRegisterRequest);
        return userId;
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if (user == null){
            log.warn("User {} not found", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!user.getPassword().equals(userLoginRequest.getPassword())){
            log.warn("User {} password not match!", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        User user = userDao.getUserById(userId);
        return user;
    }
}
