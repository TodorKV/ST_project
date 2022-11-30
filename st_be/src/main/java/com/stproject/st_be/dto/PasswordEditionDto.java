package com.stproject.st_be.dto;

import com.stproject.st_be.dto.base.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEditionDto extends BaseDto {

    private String oldPassword;
    private String newPassword;
}
