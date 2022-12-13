package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.StockDto;
import com.stproject.st_be.entity.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper extends BaseMapper<StockDto, Stock> {
    Stock fromDto(StockDto dto);

    StockDto toDto(Stock entity);
}
