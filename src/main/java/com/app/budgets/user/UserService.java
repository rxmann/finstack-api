package com.app.budgets.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.budgets.user.dto.UserDto;
import com.app.budgets.user.mapper.UserMapper;
import com.app.budgets.user.model.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<User> getAllUsers(Pageable pageable) {
        return userRepository.findAllBy(pageable).toList();

    }

    @Transactional(readOnly = true)
    public UserDto getUserProfile() {
        var authUser = userAuth.getCurrentUser();
        var user = userRepository.findById(authUser.getId()).orElse(null);
        return userMapper.toEntity(user);
    }
}
