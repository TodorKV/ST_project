package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.SimpleUserDto;
import com.stproject.st_be.dto.UserDto;
import com.stproject.st_be.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromDto(UserDto dto);

    UserDto toDto(User entity);

    SimpleUserDto toSimpleDto(User entity);
}