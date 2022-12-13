package com.stproject.st_be.services.impl;

import java.util.stream.Collectors;

import com.stproject.st_be.dto.UserDto;
import com.stproject.st_be.entity.User;
import com.stproject.st_be.entity.enums.Role;
import com.stproject.st_be.exception.NotFoundException;
import com.stproject.st_be.mappers.UserMapper;
import com.stproject.st_be.repositories.UserRepository;
import com.stproject.st_be.services.base.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserService implementation.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findByUsername(String username) {
        return userMapper.toDto(userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " was not found!")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " was not found!"));
    }

    @Override
    public Page<UserDto> searchByUsernamePaginated(String username, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<User> pagedSearchResult = this.userRepository.findByUsernameIgnoreCaseContaining(username, paging);
        Page<UserDto> dtoPagedResult = new PageImpl<>(
                pagedSearchResult.getContent().stream().map(userMapper::toDto).collect(Collectors.toList()),
                paging,
                pagedSearchResult.getTotalElements());
        return dtoPagedResult;
    }

    @Override
    public Page<UserDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<User> pagedFindResult = this.userRepository.findAll(paging);
        Page<UserDto> dtoPagedResult = new PageImpl<>(
                pagedFindResult.getContent().stream().map(userMapper::toDto).collect(Collectors.toList()),
                paging,
                pagedFindResult.getTotalElements());
        return dtoPagedResult;
    }

    public UserDto saveAndReturnAsDto(UserDto dto) {
        User user = userMapper.fromDto(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    public void promote(Integer id, Role role) {
        User user = findById(id);
        user.setRole(role);
        userRepository.save(user);
    }

    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        User user = findById(id);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean changePasswordAdmin(Integer id, String newPassword) {
        User user = findById(id);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private User findById(Integer id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    public String getTenantId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return findByUsername(auth.getPrincipal().toString())
                .getTenant()
                .getId();
    }

}
