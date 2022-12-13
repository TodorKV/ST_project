package com.stproject.st_be.services.impl;

import com.stproject.st_be.dto.ActionDto;
import com.stproject.st_be.dto.ActionStatusDto;
import com.stproject.st_be.dto.OrderDto;
import com.stproject.st_be.entity.Action;
import com.stproject.st_be.entity.ActionStatus;
import com.stproject.st_be.entity.Order;
import com.stproject.st_be.entity.Product;
import com.stproject.st_be.entity.enums.Progress;
import com.stproject.st_be.entity.enums.TimeTakenState;
import com.stproject.st_be.exception.NotFoundException;
import com.stproject.st_be.mappers.ActionMapper;
import com.stproject.st_be.mappers.ActionStatusMapper;
import com.stproject.st_be.mappers.OrderMapper;
import com.stproject.st_be.repositories.ActionStatusRepository;
import com.stproject.st_be.services.base.ActionStatusService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ActionStatusServiceImpl extends BaseServiceAbstrImpl<ActionStatusDto, ActionStatus>
        implements ActionStatusService {

    private static final int EXCEEDED_MINUTES_FOR_YELLOW_FLAG = 10;

    private final ActionStatusRepository repository;
    private final OrderServiceImpl orderService;
    private final ActionStatusMapper mapper;
    private final ActionMapper actionMapper;
    private final OrderMapper orderMapper;

    public ActionStatusServiceImpl(ActionStatusRepository repository,
            ActionStatusMapper mapper,
            ActionMapper actionMapper,
            OrderMapper orderMapper,
            OrderServiceImpl orderService) {
        this.repository = repository;
        this.mapper = mapper;
        this.actionMapper = actionMapper;
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    @Override
    public ActionStatusDto save(ActionStatusDto dto, ActionDto actionDto, OrderDto orderDto) {
        ActionStatus entity = mapper.fromDto(dto);
        entity.setAction(actionMapper.fromDto(actionDto));
        entity.setOrder(orderMapper.fromDto(orderDto));
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public Page<ActionStatusDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }

    @Override
    public ActionStatusDto edit(Integer targetId, ActionStatusDto sourceDto) {
        ActionStatus target = repository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(Order.class, targetId));

        advanceProgress(target);
        if (target.getProgress().equals(Progress.COMPLETED))
            checkIfOrderIsCompleted(target);

        return mapper.toDto(repository.save(target));
    }

    @Override
    public ActionStatusDto findById(Integer targetId) {
        return mapper.toDto(repository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(Product.class, targetId)));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    private void advanceProgress(ActionStatus target) {
        if (target.getProgress().equals(Progress.NOT_STARTED)) {
            target.setTimeBegin(new Date());

            target.setProgress(Progress.CURRENTLY_WORKING);
        } else if (target.getProgress().equals(Progress.CURRENTLY_WORKING)) {
            Date timeBegin = target.getTimeBegin();
            Date timeEnd = new Date();
            target.setTimeEnd(timeEnd);

            long timeTakenInMilliseconds = Math.abs(timeEnd.getTime() - timeBegin.getTime());
            long expectedTimeInMilliseconds = TimeUnit.MILLISECONDS.convert(target.getAction().getExpectedMinutes(), TimeUnit.MINUTES);
            long allowedExceedTimeInMilliseconds = TimeUnit.MILLISECONDS.convert(EXCEEDED_MINUTES_FOR_YELLOW_FLAG, TimeUnit.MINUTES);

            if (timeTakenInMilliseconds <= expectedTimeInMilliseconds) {
                target.setTimeTakenState(TimeTakenState.GREEN);
            } else if (timeTakenInMilliseconds <= expectedTimeInMilliseconds + allowedExceedTimeInMilliseconds) {
                target.setTimeTakenState(TimeTakenState.YELLOW);
            } else {
                target.setTimeTakenState(TimeTakenState.RED);
            }

            target.setProgress(Progress.COMPLETED);
        }
    }

    private void checkIfOrderIsCompleted(ActionStatus currenActionStatus) {
        // actions are ordered by step on retrieve so the last one should always be with
        // the highest step
        Set<Action> actions = currenActionStatus.getOrder().getProduct().getActions();
        Action last = new Action();
        Iterator<Action> actionsIterator = actions.iterator();
        while (actionsIterator.hasNext())
            last = actionsIterator.next();

        if (last.getStep() == currenActionStatus.getAction().getStep()) {
            Order order = currenActionStatus.getOrder();
            order.setCompleted(true);
            this.orderService.save(this.orderMapper.toDto(order));
        }
    }
}
