package com.stproject.st_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto implements Serializable {

    private static final long serialVersionUID = 4L;

    @NonNull
    private String id;
    private String tenantValue;
    private SimpleUserDto user;
}
