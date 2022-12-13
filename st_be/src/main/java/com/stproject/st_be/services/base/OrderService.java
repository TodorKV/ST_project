package com.stproject.st_be.service.base;

import java.util.List;

import com.stproject.st_be.dto.OrderDto;
import org.springframework.data.domain.Page;

public interface OrderService extends BaseService<OrderDto> {

        Page<OrderDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy, boolean finished);

        Page<OrderDto> searchByDescriptionPaginated(String description, Integer pageNo, Integer pageSize, String sortBy,
                        boolean isCompleted);

        Page<OrderDto> getAllWhereTenantId(Integer pageNo, Integer pageSize, String sortBy);

        Page<OrderDto> searchAllWhereTenantIdAndDescription(String description, Integer pageNo, Integer pageSize,
                        String sortBy);
                        
        List<OrderDto> findAllCompleted();
}
