package com.stproject.st_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverdueTaskDto {
    private String description;
    private Date whenToBeDone;
    private Date finishedOnDate;
    private String realName;
}
