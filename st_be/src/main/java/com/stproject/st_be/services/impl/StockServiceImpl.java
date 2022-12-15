package com.stproject.st_be.services.impl;

import com.stproject.st_be.dto.StockDto;
import com.stproject.st_be.entity.Stock;
import com.stproject.st_be.mappers.StockMapper;
import com.stproject.st_be.repositories.StockRepository;
import com.stproject.st_be.services.base.StockService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends BaseServiceAbstrImpl<StockDto, Stock> implements StockService {
    private final StockRepository repository;
    private final StockMapper mapper;

    public StockServiceImpl(StockRepository repository,
            StockMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        super.setMapper(mapper);
        super.setRepository(repository);
    }

    @Override
    public Page<StockDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }
}
