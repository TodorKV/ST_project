package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.ActionStatusDto;
import com.stproject.st_be.entity.ActionStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActionStatusMapper extends BaseMapper<ActionStatusDto, ActionStatus> {
    ActionStatus fromDto(ActionStatusDto dto);

    ActionStatusDto toDto(ActionStatus entity);
}