package com.stproject.st_be.services.impl;

import com.stproject.st_be.dto.base.BaseDto;
import com.stproject.st_be.entity.Order;
import com.stproject.st_be.entity.base.BaseEntity;
import com.stproject.st_be.exception.NotFoundException;
import com.stproject.st_be.mappers.BaseMapper;
import com.stproject.st_be.services.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public abstract class BaseServiceAbstrImpl<D extends BaseDto, E extends BaseEntity> implements BaseService<D> {

    private JpaRepository<E, Integer> repository;
    private BaseMapper<D, E> mapper;

    protected void setMapper(BaseMapper<D, E> mapper) {
        this.mapper = mapper;
    }

    protected void setRepository(JpaRepository<E, Integer> repository) {
        this.repository = repository;
    }

    @Override
    public D save(D dto) {
        E entity = mapper.fromDto(dto);
        E saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public Page<D> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<E> pagedResult = this.repository.findAll(paging);
        Page<D> dtoPagedResult = new PageImpl<>(
                pagedResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                paging,
                pagedResult.getTotalElements());

        return dtoPagedResult;
    }

    @Override
    public D edit(Integer targetId, D sourceDto) {
        E source = mapper.fromDto(sourceDto);
        E target = repository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(Order.class, targetId));
        BeanUtils.copyProperties(source, target, "id");
        return mapper.toDto(repository.save(target));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public D findById(Integer targetId) {
        return mapper.toDto(repository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(BaseEntity.class, targetId)));
    }
}