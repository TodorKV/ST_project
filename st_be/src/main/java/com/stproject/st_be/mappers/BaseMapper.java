package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.base.BaseDto;
import com.stproject.st_be.entity.base.BaseEntity;

public interface BaseMapper <D extends BaseDto, E extends BaseEntity> {
    E fromDto(D dto);
    D toDto(E entity);
}