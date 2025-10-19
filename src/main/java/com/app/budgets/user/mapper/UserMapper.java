package com.app.budgets.user.mapper;

import org.mapstruct.Mapper;

import com.app.budgets.user.dto.UserDto;
import com.app.budgets.user.dto.UserResponse;
import com.app.budgets.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toEntity(User user);

    UserResponse toResponse(UserDto userDto);

}
