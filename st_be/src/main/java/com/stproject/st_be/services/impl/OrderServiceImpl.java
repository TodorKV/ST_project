package com.stproject.st_be.services.impl;

import com.stproject.st_be.dto.ActionStatusDto;
import com.stproject.st_be.dto.OrderDto;
import com.stproject.st_be.entity.Order;
import com.stproject.st_be.entity.enums.Progress;
import com.stproject.st_be.mappers.OrderMapper;
import com.stproject.st_be.repositories.OrderRepository;
import com.stproject.st_be.services.base.OrderService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends BaseServiceAbstrImpl<OrderDto, Order> implements OrderService {

    private final OrderRepository repository;
    private final UserServiceImpl userService;
    private final OrderMapper mapper;

    public OrderServiceImpl(OrderRepository repository,
            OrderMapper mapper,
            UserServiceImpl userService) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
        super.setMapper(this.mapper);
        super.setRepository(this.repository);
    }

    public Page<OrderDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy, boolean isCompleted) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> pagedResult = this.repository.findAll(paging, isCompleted, createDateBeforeFlag());
        Page<OrderDto> dtoPagedResult = new PageImpl<>(
                pagedResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                paging,
                pagedResult.getTotalElements());

        markLastActionStatusDto(dtoPagedResult);
        markReadyForWorkTrueWhereNeeded(dtoPagedResult);
        return dtoPagedResult;
    }

    @Override
    public Page<OrderDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        // TODO implement search by orderNumber or description
        return null;
    }

    @Override
    public Page<OrderDto> searchByDescriptionPaginated(String description, Integer pageNo, Integer pageSize,
            String sortBy, boolean isCompleted) {
        // TODO implement search by orderNumber or description
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> pagedSearchResult = this.repository.findByDescriptionIgnoreCaseContaining(description, paging,
                isCompleted, createDateBeforeFlag());
        Page<OrderDto> dtoPagedResult = new PageImpl<>(
                pagedSearchResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                paging,
                pagedSearchResult.getTotalElements());
        markLastActionStatusDto(dtoPagedResult);
        return dtoPagedResult;
    }

    @Override
    public Page<OrderDto> getAllWhereTenantId(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        String tenantid = userService.getTenantId();
        Page<Order> pagedFindResult = this.repository.findAllWhereTenantId(tenantid, paging,
                Progress.COMPLETED, createDateBeforeFlag());
        List<OrderDto> orders = pagedFindResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
        Page<OrderDto> dtoPagedResult = new PageImpl<>(
                orders,
                paging,
                pagedFindResult.getTotalElements());

        markLastActionStatusDto(dtoPagedResult);
        markReadyForWorkTrueWhereNeeded(dtoPagedResult);
        filterActionStatusesWhereTenantNotIncluded(dtoPagedResult, tenantid);
        return dtoPagedResult;
    }

    @Override
    public Page<OrderDto> searchAllWhereTenantIdAndDescription(String description, Integer pageNo, Integer pageSize,
            String sortBy) {
        String tenantid = userService.getTenantId();
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> pagedSearchResult = this.repository.findAllWhereTenantIdAndDescription(description, tenantid,
                paging, Progress.COMPLETED, createDateBeforeFlag());
        List<OrderDto> orders = pagedSearchResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
        Page<OrderDto> dtoPagedResult = new PageImpl<>(
                orders,
                paging,
                pagedSearchResult.getTotalElements());

        markLastActionStatusDto(dtoPagedResult);
        markReadyForWorkTrueWhereNeeded(dtoPagedResult);
        filterActionStatusesWhereTenantNotIncluded(dtoPagedResult, tenantid);
        return dtoPagedResult;
    }

    private void filterActionStatusesWhereTenantNotIncluded(Page<OrderDto> dtoPagedResult, String tenantId) {
        List<OrderDto> dtoEntityContent = dtoPagedResult.getContent();
        for (OrderDto order : dtoEntityContent) {
            Set<ActionStatusDto> actionStatuses = order.getActionStatuses();
            Set<ActionStatusDto> toRemove = new HashSet<>();
            for (ActionStatusDto actionStatus : actionStatuses) {
                // because for now we only have one tenant in each Set<Tenants in actionStatus
                if (!actionStatus.getTenants().iterator().next().getId().equals(tenantId)) {
                    toRemove.add(actionStatus);
                }
            }
            actionStatuses.removeAll(toRemove);
            order.setActionStatuses(actionStatuses);
        }
    }

    private void markReadyForWorkTrueWhereNeeded(Page<OrderDto> dtoPagedResult) {
        List<OrderDto> orderDtosContent = dtoPagedResult.getContent();
        for (OrderDto orderDto : orderDtosContent) {
            Set<ActionStatusDto> actionStatuses = orderDto.getActionStatuses();

            List<ActionStatusDto> actionStatusDtosAsList = new LinkedList<>(actionStatuses);

            for (ActionStatusDto actionStatusDto : actionStatusDtosAsList) {
                if (!actionStatusDto.getProgress().equals(Progress.NOT_STARTED)) {
                    actionStatusDto.setIsReadyForWork(true);
                }
            }

            if (actionStatusDtosAsList.size() > 0) {
                ActionStatusDto firstActionStatusDTO = actionStatusDtosAsList.get(0);
                if (firstActionStatusDTO.getProgress().equals(Progress.NOT_STARTED)) {
                    firstActionStatusDTO.setIsReadyForWork(true);
                    actionStatusDtosAsList.set(0, firstActionStatusDTO);
                } else {
                    for (int i = 1; i < actionStatusDtosAsList.size(); i++) {
                        if (actionStatusDtosAsList.get(i - 1).getProgress().equals(Progress.COMPLETED)) {
                            ActionStatusDto actionStatusDto = actionStatusDtosAsList.get(i);
                            actionStatusDto.setIsReadyForWork(true);
                            actionStatusDtosAsList.set(i, actionStatusDto);
                        }
                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public List<OrderDto> findAllCompleted() {
        return this.repository.findAllCompleted().stream().map(o -> mapper.toDto(o)).collect(Collectors.toList());
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

    private void markLastActionStatusDto(Page<OrderDto> dtoPagedResult) {
        for (OrderDto orderDto : dtoPagedResult.getContent()) {
            if (orderDto.getActionStatuses().size() > 0) {
                ActionStatusDto actionStatusDto = (ActionStatusDto) orderDto.getActionStatuses()
                        .toArray()[orderDto.getActionStatuses().size() - 1];

                actionStatusDto.setLastAction(true);
            }
        }
    }

}
