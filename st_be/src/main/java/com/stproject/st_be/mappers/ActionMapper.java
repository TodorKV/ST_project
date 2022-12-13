package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.ActionDto;
import com.stproject.st_be.entity.Action;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActionMapper extends BaseMapper<ActionDto, Action> {
    Action fromDto(ActionDto dto);

    ActionDto toDto(Action entity);
}