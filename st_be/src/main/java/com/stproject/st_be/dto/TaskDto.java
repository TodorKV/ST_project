package com.stproject.st_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stproject.st_be.dto.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TaskDto extends BaseDto {

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date whenToBeDone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date finishedOnDate;

    private boolean completed;

    private Set<TenantDto> tenants = new HashSet<>();
}