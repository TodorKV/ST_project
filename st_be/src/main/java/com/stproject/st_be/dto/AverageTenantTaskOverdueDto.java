package com.stproject.st_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageTenantTaskOverdueDto {
    String tenantId;

    Long averageTaskOverdue;
}
