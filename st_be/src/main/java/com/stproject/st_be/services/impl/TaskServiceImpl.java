package com.stproject.st_be.services.impl;

import com.stproject.st_be.dto.TaskDto;
import com.stproject.st_be.entity.Task;
import com.stproject.st_be.mappers.TaskMapper;
import com.stproject.st_be.repositories.TaskRepository;
import com.stproject.st_be.services.base.TaskService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
