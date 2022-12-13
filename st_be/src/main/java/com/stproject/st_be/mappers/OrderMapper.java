package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.OrderDto;
import com.stproject.st_be.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PhotoMapper.class)
public interface OrderMapper extends BaseMapper<OrderDto, Order> {
    Order fromDto(OrderDto dto);

    OrderDto toDto(Order entity);
}