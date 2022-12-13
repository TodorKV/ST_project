package com.stproject.st_be.service.base;

import com.stproject.st_be.dto.TaskDto;
import org.springframework.data.domain.Page;

public interface TaskService extends BaseService<TaskDto>{

    Page<TaskDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy, boolean isCompleted);

    Page<TaskDto> searchByDescriptionPaginated(String description, Integer pageNo, Integer pageSize, String sortBy,
                                                boolean isCompleted);

    Page<TaskDto> getAllWhereTenantId(Integer pageNo, Integer pageSize, String sortBy);

    Page<TaskDto> searchAllWhereTenantIdAndDescription(String description, Integer pageNo, Integer pageSize,
                                                        String sortBy);

}
