package com.app.budgets.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.budgets.user.dto.UserDto;
import com.app.budgets.user.mapper.UserMapper;
import com.app.budgets.user.model.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private UserAuth userAuth;
    private UserMapper userMapper;

    UserService(UserMapper userMapper, UserRepository userRepository, UserAuth userAuth) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userAuth = userAuth;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;

    }

    public UserDto getUserProfile() {
        var authUser = userAuth.getCurrentUser();
        var user = userRepository.findById(authUser.getId()).orElse(null);
        return userMapper.toEntity(user);
    }
}
