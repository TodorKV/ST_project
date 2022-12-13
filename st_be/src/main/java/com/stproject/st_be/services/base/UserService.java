package com.stproject.st_be.services.base;

import com.stproject.st_be.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto findByUsername(String username);

    Page<UserDto> searchByUsernamePaginated(String username, Integer pageNo, Integer pageSize, String sortBy);

    Page<UserDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy);
}
