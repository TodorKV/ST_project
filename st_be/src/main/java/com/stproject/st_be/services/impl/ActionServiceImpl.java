package com.stproject.st_be.service.impl;

import java.util.stream.Collectors;

import com.stproject.st_be.dto.ActionDto;
import com.stproject.st_be.entity.Action;
import com.stproject.st_be.entity.Product;
import com.stproject.st_be.exception.NotFoundException;
import com.stproject.st_be.mappers.ActionMapper;
import com.stproject.st_be.repository.ActionRepository;
import com.stproject.st_be.repository.ProductRepository;
import com.stproject.st_be.service.base.ActionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ActionServiceImpl extends BaseServiceAbstrImpl<ActionDto, Action> implements ActionService {

    private final ActionRepository repository;
    private final ProductRepository productRepository;
    private final ActionMapper mapper;

    public ActionServiceImpl(ActionRepository repository,
            ActionMapper mapper,
            ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.mapper = mapper;

        super.setRepository(this.repository);
        super.setMapper(this.mapper);
    }

    public ActionDto save(ActionDto dto, Integer productId) {
        Action entity = mapper.fromDto(dto);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, productId));

        entity.setProduct(product);

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public Page<ActionDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Action> pagedSearchResult = this.repository.findByNameIgnoreCaseContaining(name, paging);
        Page<ActionDto> dtoPagedResult = new PageImpl<>(
                pagedSearchResult.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                paging,
                pagedSearchResult.getTotalElements());
        return dtoPagedResult;
    }
}
