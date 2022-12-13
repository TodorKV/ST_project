package com.stproject.st_be.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import com.stproject.st_be.dto.ActionDto;
import com.stproject.st_be.dto.ProductDto;
import com.stproject.st_be.entity.Action;
import com.stproject.st_be.entity.Order;
import com.stproject.st_be.entity.Product;
import com.stproject.st_be.exception.NotFoundException;
import com.stproject.st_be.mappers.ActionMapper;
import com.stproject.st_be.mappers.ProductMapper;
import com.stproject.st_be.repository.ActionRepository;
import com.stproject.st_be.repository.ProductRepository;
import com.stproject.st_be.service.base.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ActionRepository actionRepository;
    private final ProductMapper mapper;
    private final ActionMapper actionMapper;

    public ProductServiceImpl(ProductRepository repository,
            ProductMapper mapper,
            ActionRepository actionRepository,
            ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.repository = repository;
        this.actionMapper = actionMapper;
        this.mapper = mapper;
    }

    /**
     * OneToMany annotation requires the parent object to be saved first so that the
     * child annotions can be mapped
     * to him so:
     * First we retrieve the actions from the passed product dto
     * then we save the product dto to the DB. Once it's save then we can use its ID
     * to set it to the retrieved actions.
     * Then we save them to db.
     */
    @Override
    public ProductDto save(ProductDto dto) {
        Product entity = mapper.fromDto(dto);
        Set<Action> actions = entity.getActions();
        entity = repository.save(entity);
        Product finalEntity = entity;
        actions.stream().forEach(a -> a.setProduct(finalEntity));
        Set<ActionDto> actionDtos = actions.stream().map(actionRepository::save).map(actionMapper::toDto)
                .collect(Collectors.toSet());

        ProductDto result = mapper.toDto(entity);
        result.setActions(actionDtos);
        return result;
    }

    @Override
    public Page<ProductDto> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = this.repository.findAll(paging);
        Page<ProductDto> dtoPagedResult = new PageImpl<>(
                pagedResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                paging,
                pagedResult.getTotalElements());

        return dtoPagedResult;
    }

    @Override
    public ProductDto edit(Integer targetId, ProductDto sourceDto) {
        Product source = mapper.fromDto(sourceDto);
        Product target = repository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(Order.class, targetId));
        BeanUtils.copyProperties(source, target, "id");
        return mapper.toDto(repository.save(target));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Page<ProductDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedSearchResult = this.repository.findByNameIgnoreCaseContaining(name, paging);
        Page<ProductDto> dtoPagedResult = new PageImpl<>(
                pagedSearchResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                paging,
                pagedSearchResult.getTotalElements());
        return dtoPagedResult;
    }

    @Override
    public ProductDto findById(Integer targetId) {
        return mapper.toDto(repository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(Product.class, targetId)));
    }
}
