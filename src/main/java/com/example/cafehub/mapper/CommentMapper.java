package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface CommentMapper {
    @Mapping(target = "time", source = "time", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CommentResponseDto toDto(Comment comment);
}
