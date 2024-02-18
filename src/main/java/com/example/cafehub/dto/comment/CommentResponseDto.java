package com.example.cafehub.dto.comment;

import com.example.cafehub.dto.user.UserResponseDto;
import lombok.Data;

@Data
public class CommentResponseDto {
    private Long id;
    private String time;
    private UserResponseDto user;
    private String comment;
}
