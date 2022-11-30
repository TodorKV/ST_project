package com.stproject.st_be.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stproject.st_be.dto.base.BaseDto;
import com.stproject.st_be.entity.enums.Priority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderDto extends BaseDto {

    private static final long serialVersionUID = 4L;

    private String description;
    private String orderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date whenToBeDone;
    private Set<PhotoDto> photos;
    private ProductDto product;
    private Priority priority;
    private List<String> tenantIds;
    private Set<ActionStatusDto> actionStatuses;
    private boolean completed;
}
