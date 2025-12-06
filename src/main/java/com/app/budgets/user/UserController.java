package com.app.budgets.user;

import java.util.List;

import com.app.budgets.budget.dto.RecurringBudgetResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.budgets.user.dto.UserResponse;
import com.app.budgets.user.mapper.UserMapper;
import com.app.budgets.user.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    UserController(UserMapper userMapper, UserService userService) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(Pageable pageable) {
        var users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        var userDto = userService.getUserProfile();
        return ResponseEntity.ok(userMapper.toResponse(userDto));
    }
}
