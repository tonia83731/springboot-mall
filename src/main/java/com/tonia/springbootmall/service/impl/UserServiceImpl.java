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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.print.DocFlavor;


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

        // use MD5 hashed password
        // String password = userRegisterRequest.getPassword();
        // String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(hashedPassword);

        Integer userId = userDao.createUser(userRegisterRequest);
        return userId;
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        // check if user exists
        if (user == null){
            log.warn("User {} not found", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // check user password
        // String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        // if (!user.getPassword().equals(hashedPassword)){
        //     log.warn("User {} password not match!", userLoginRequest.getEmail());
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        // }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userLoginRequest.getPassword(),user.getPassword())){
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
