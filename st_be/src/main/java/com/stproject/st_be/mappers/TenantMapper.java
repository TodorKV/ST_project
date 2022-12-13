package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.TenantDto;
import com.stproject.st_be.entity.Tenant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TenantMapper {
    Tenant fromDto(TenantDto dto);
    TenantDto toDto(Tenant entity);
}
