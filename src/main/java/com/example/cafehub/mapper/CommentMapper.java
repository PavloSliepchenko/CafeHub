package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.model.Comment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface CommentMapper {
    CommentResponseDto toDto(Comment comment);
}
