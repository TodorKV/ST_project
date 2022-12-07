package com.stproject.st_be.services.base;

import org.springframework.data.domain.Page;

public interface BaseService<D> {
    Page<D> getAllPaginated(Integer pageNo, Integer pageSize, String sortBy);
    Page<D> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy);
    D save(D dto);
    D edit(Integer targetId, D sourceDto);
    D findById(Integer targetId);
    void deleteById(Integer id);
}
