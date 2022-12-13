package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.TaskDto;
import com.stproject.st_be.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper  extends BaseMapper<TaskDto, Task> {
    Task fromDto(TaskDto dto);

    TaskDto toDto(Task entity);
}