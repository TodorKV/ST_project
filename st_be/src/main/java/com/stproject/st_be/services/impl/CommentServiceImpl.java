package com.stproject.st_be.service.impl;

import com.stproject.st_be.dto.CommentDto;
import com.stproject.st_be.entity.Comment;
import com.stproject.st_be.mappers.CommentMapper;
import com.stproject.st_be.repository.CommentRepository;
import com.stproject.st_be.service.base.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CommentServiceImpl extends BaseServiceAbstrImpl<CommentDto, Comment> implements CommentService {

    private final CommentMapper mapper;
    private final CommentRepository repository;

    public CommentServiceImpl(CommentMapper mapper, CommentRepository repository) {
        this.mapper = mapper;
        this.repository = repository;

        super.setMapper(this.mapper);
        super.setRepository(this.repository);
    }

    @Override
    public CommentDto save(CommentDto dto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd' 'HH:mm");
        String currentDate = formatter.format(new Date());

        dto.setTimeSent(currentDate);

        Comment entity = mapper.fromDto(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public Page<CommentDto> searchByNamePaginated(String name, Integer pageNo, Integer pageSize, String sortBy) {
        return null;
    }
}
