package com.stproject.st_be.dto;

import com.stproject.st_be.dto.base.BaseDto;
import lombok.*;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SimpleUserDto extends BaseDto {

    private String username;
}
