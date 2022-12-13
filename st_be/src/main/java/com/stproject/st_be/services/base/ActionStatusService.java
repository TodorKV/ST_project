package com.stproject.st_be.service.base;

import com.stproject.st_be.dto.ActionDto;
import com.stproject.st_be.dto.ActionStatusDto;
import com.stproject.st_be.dto.OrderDto;

public interface ActionStatusService extends BaseService<ActionStatusDto> {
    ActionStatusDto save(ActionStatusDto dto, ActionDto actionDto, OrderDto orderDto);

    ActionStatusDto edit(Integer targetId, ActionStatusDto sourceDto);

    ActionStatusDto findById(Integer targetId);

    void deleteById(Integer id);
}
