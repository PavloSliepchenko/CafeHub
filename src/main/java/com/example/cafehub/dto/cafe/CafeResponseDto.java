package com.example.cafehub.dto.cafe;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CafeResponseDto {
    private Long id;
    private String name;
    private String address;
    private List<LanguageResponseDto> languages;
    private String workHoursWeekDays;
    private String workHoursWeekends;
    private int score;
    private String urlOfImage;
    private List<CommentResponseDto> comments;
    private String webSite;
    private boolean coworking;
    private boolean vegan;
    private boolean petFriendly;
    private BigDecimal averageBill;
    private List<String> images;
    private boolean childZone;
    private boolean smokingFriendly;
}
