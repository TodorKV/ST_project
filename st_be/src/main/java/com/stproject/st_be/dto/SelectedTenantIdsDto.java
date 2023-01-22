package com.stproject.st_be.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SelectedTenantIdsDto {
    List<String> tenantIds;
}
