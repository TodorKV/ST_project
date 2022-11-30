package com.stproject.st_be.dto;

import com.stproject.st_be.dto.base.BaseDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ActionDto extends BaseDto {

    private String name;
    private int expectedMinutes;
    private int step;
    // @JsonIgnore
    // private ProductDto product;
    // private Set<TenantDto> tenants;
    // private Set<ActionStatusDto> actionStatuses;
}