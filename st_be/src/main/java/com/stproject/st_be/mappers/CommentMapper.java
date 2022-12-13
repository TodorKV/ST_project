package com.stproject.st_be.mappers;

import com.stproject.st_be.dto.CommentDto;
import com.stproject.st_be.dto.SimpleCommentDto;
import com.stproject.st_be.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends BaseMapper<CommentDto, Comment> {
    Comment fromDto(CommentDto dto);

    @Mapping(target = "actionStatus", ignore = true )
    CommentDto toDto(Comment entity);

    SimpleCommentDto toSimpleDto(Comment entity);
}
