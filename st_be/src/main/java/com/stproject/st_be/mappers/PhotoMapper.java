package com.stproject.st_be.mappers;


import com.stproject.st_be.dto.PhotoDto;
import com.stproject.st_be.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper extends BaseMapper<PhotoDto, Photo> {
    Photo fromDto(PhotoDto dto);

    PhotoDto toDto(Photo entity);
}
