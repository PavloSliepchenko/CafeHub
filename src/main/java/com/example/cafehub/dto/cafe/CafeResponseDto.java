package com.example.cafehub.dto.cafe;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.cuisine.CuisineResponseDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class CafeResponseDto {
    private Long id;
    private String name;
    private String address;
    private List<LanguageResponseDto> languages;
    private LocalTime openFromWeekdays;
    private LocalTime closeAtWeekdays;
    private LocalTime openFromWeekends;
    private LocalTime closeAtWeekends;
    private BigDecimal score;
    private String urlToGoogleMaps;
    private String urlOfImage;
    private List<CommentResponseDto> comments;
    private String webSite;
    private boolean coworking;
    private boolean vegan;
    private boolean petFriendly;
    private BigDecimal averageBill;
    private List<String> images;
    private List<CuisineResponseDto> cuisines;
    private boolean fastService;
    private boolean wifi;
    private boolean businessLunch;
    private boolean freeWater;
    private boolean boardGames;
    private boolean birthday;
    private boolean businessMeeting;
    private boolean childHoliday;
    private boolean romantic;
    private boolean thematicEvent;
    private boolean familyHoliday;
    private boolean parking;
    private boolean terrace;
    private String description;
}
