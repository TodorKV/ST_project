package com.stproject.st_be.services.impl;

import com.stproject.st_be.dto.AverageTenantTaskOverdueDto;
import com.stproject.st_be.dto.TaskDto;
import com.stproject.st_be.entity.Task;
import com.stproject.st_be.mappers.TaskMapper;
import com.stproject.st_be.repositories.TaskRepository;
import com.stproject.st_be.services.base.TaskService;
import com.stproject.st_be.utils.OverdueTaskComparator;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends BaseServiceAbstrImpl<TaskDto, Task> implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserServiceImpl userService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           UserServiceImpl userService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        super.setMapper(taskMapper);
        super.setRepository(taskRepository);
    }

    @Override
    public Page<TaskDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy, boolean isCompleted) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Task> pagedResult = this.taskRepository.findAll(paging, isCompleted, createDateBeforeFlag());
        Page<TaskDto> dtoPagedResult = new PageImpl<>(
                pagedResult.getContent().stream().map(taskMapper::toDto).collect(Collectors.toList()),
                paging,
                pagedResult.getTotalElements());

        return dtoPagedResult;
    }

    @Override
    public Page<TaskDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        // TODO implement search by TaskNumber or description
        return null;
    }

    @Override
    public Page<TaskDto> searchByDescriptionPaginated(String description, Integer pageNo, Integer pageSize,
                                                      String sortBy, boolean isCompleted) {
        // TODO implement search by TaskNumber or description
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Task> pagedSearchResult = this.taskRepository.findByDescriptionIgnoreCaseContaining(description,
                paging,
                isCompleted, createDateBeforeFlag());
        Page<TaskDto> dtoPagedResult = new PageImpl<>(
                pagedSearchResult.getContent().stream().map(taskMapper::toDto)
                        .collect(Collectors.toList()),
                paging,
                pagedSearchResult.getTotalElements());
        return dtoPagedResult;
    }

    @Override
    public Page<TaskDto> getAllWhereTenantId(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        String tenantid = userService.getTenantId();
        Page<Task> pagedFindResult = this.taskRepository.findAllWhereTenantId(tenantid, paging,
                createDateBeforeFlag(),
                false);
        List<TaskDto> tasks = pagedFindResult.getContent().stream().map(taskMapper::toDto)
                .collect(Collectors.toList());
        Page<TaskDto> dtoPagedResult = new PageImpl<>(
                tasks,
                paging,
                pagedFindResult.getTotalElements());
        return dtoPagedResult;
    }

    @Override
    public Page<TaskDto> searchAllWhereTenantIdAndDescription(String description, Integer pageNo, Integer pageSize,
                                                              String sortBy) {
        String tenantid = userService.getTenantId();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Task> pagedSearchResult = this.taskRepository.findAllWhereTenantIdAndDescription(description,
                tenantid,
                paging, createDateBeforeFlag(), false);
        List<TaskDto> tasks = pagedSearchResult.getContent().stream().map(taskMapper::toDto)
                .collect(Collectors.toList());
        Page<TaskDto> dtoPagedResult = new PageImpl<>(
                tasks,
                paging,
                pagedSearchResult.getTotalElements());

        return dtoPagedResult;
    }

    @Override
    public Page<TaskDto> getAllOverdueTasksWhereTenantId(Integer pageNo, Integer pageSize, String tenantId) {
        List<Task> taskEntitiesPerTenant = taskRepository.findAllOverdueTasksWhereTenantId(tenantId).stream()
                .sorted(new OverdueTaskComparator())
                .collect(Collectors.toList());

        List<TaskDto> overdueTaskDtosList = taskEntitiesPerTenant.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(overdueTaskDtosList, PageRequest.of(pageNo, pageSize), taskEntitiesPerTenant.size());
    }

    @Override
    public Page<AverageTenantTaskOverdueDto> getOverdueTasksAverageStatisticsForTenants(Integer pageNo,
                                                                                        Integer pageSize,
                                                                                        List<String> tenantIds) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<AverageTenantTaskOverdueDto> list = new ArrayList<>();

        for (String tenantId : tenantIds) {
            List<Task> taskEntitiesPerTenant = taskRepository.findAllOverdueTasksWhereTenantId(tenantId);

            BigInteger averageOverduePerTenant = getAverageOverduePerTenant(taskEntitiesPerTenant);

            list.add(new AverageTenantTaskOverdueDto(tenantId, averageOverduePerTenant.longValue()));
        }


        return new PageImpl<>(list, pageable, list.size());
    }

    private BigInteger getAverageOverduePerTenant(List<Task> taskEntitiesPerTenant) {
        return taskEntitiesPerTenant.stream()
                .map(task -> Period.between(task.getWhenToBeDone().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        task.getFinishedOnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays())
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add)
                .divide(BigInteger.valueOf(taskEntitiesPerTenant.size()));
    }

    private Date createDateBeforeFlag() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        return calendar.getTime();
    }
}
