package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.ProductDto;
import com.stproject.st_be.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ActionMapper.class)
public interface ProductMapper extends BaseMapper<ProductDto, Product>{
    Product fromDto(ProductDto dto);
    ProductDto toDto(Product entity);
}