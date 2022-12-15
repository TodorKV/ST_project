package com.stproject.st_be.controllers.impl;

import com.stproject.st_be.dto.PasswordEditionDto;
import com.stproject.st_be.dto.UserDto;
import com.stproject.st_be.entity.enums.Role;
import com.stproject.st_be.services.impl.UserServiceImpl;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Post Mapping
     *
     * @param dto UserDto
     * @return UserDto
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.saveAndReturnAsDto(dto), HttpStatus.CREATED);
    }

    /**
     * Get Mapping
     *
     * @return UserDto
     */
    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/promote/{id}/to/{role}")
    public ResponseEntity promoteUser(@PathVariable Integer id,
            @PathVariable Role role) {
        userService.promote(id, role);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/newpass")
    public ResponseEntity changePassword(@RequestBody PasswordEditionDto dto) {

        return userService.changePasswordAdmin(dto.getId(), dto.getNewPassword())
                ? new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/my/newpass")
    public ResponseEntity changeMyPassword(@RequestBody PasswordEditionDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer id = userService
                .findByUsername(auth.getPrincipal().toString())
                .getId();
        dto.setId(id);
        return userService.changePassword(dto.getId(), dto.getOldPassword(), dto.getNewPassword())
                ? new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getByTenantId(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<UserDto> list = this.userService.getAllPaginated(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<Page<UserDto>> searchByDescriptionPaginated(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String username) {
        Page<UserDto> list = this.userService.searchByUsernamePaginated(username, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

}
