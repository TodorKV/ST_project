package com.stproject.st_be.services.base;

import com.stproject.st_be.dto.AverageTenantTaskOverdueDto;
import com.stproject.st_be.dto.TaskDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService extends BaseService<TaskDto> {

    Page<TaskDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy, boolean isCompleted);

    Page<TaskDto> searchByDescriptionPaginated(String description, Integer pageNo, Integer pageSize, String sortBy,
                                               boolean isCompleted);

    Page<TaskDto> getAllWhereTenantId(Integer pageNo, Integer pageSize, String sortBy);

    Page<TaskDto> searchAllWhereTenantIdAndDescription(String description, Integer pageNo, Integer pageSize,
                                                       String sortBy);

    Page<TaskDto> getAllOverdueTasksWhereTenantId(Integer pageNo, Integer pageSize, String tenantID);

    AverageTenantTaskOverdueDto getOverdueTasksAverageStatisticsForTenants(String tenantId);
}
