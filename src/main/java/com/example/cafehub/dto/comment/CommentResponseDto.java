package com.example.cafehub.dto.comment;

import com.example.cafehub.dto.user.UserResponseDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CommentResponseDto {
    private Long id;
    private Long cafeId;
    private String cafeName;
    private String urlOfImage;
    private String time;
    private UserResponseDto user;
    private String comment;
    private BigDecimal score;
}
