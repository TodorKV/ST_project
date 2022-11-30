package com.stproject.st_be.dto;

import java.util.Set;

import com.stproject.st_be.dto.base.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ProductDto extends BaseDto {

    private String name;
    private String description;
    private Set<ActionDto> actions;

}
