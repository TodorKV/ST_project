package com.stproject.st_be.dto;

import com.stproject.st_be.dto.base.BaseDto;
import com.stproject.st_be.entity.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    private static final long serialVersionUID = 4L;

    private String realname;
    private String username;
    private String password;
    private Role role;
    private TenantDto tenant;
}
