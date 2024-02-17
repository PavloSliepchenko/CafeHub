package com.example.cafehub.dto.cafe;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class CreateCafeDto {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String city;
    @NotNull
    @NotEmpty
    private String address;
    private LocalTime openFromWeekdays;
    private LocalTime closeAtWeekdays;
    private LocalTime openFromWeekends;
    private LocalTime closeAtWeekends;
    private String urlToGoogleMaps;
    private String urlOfImage;
    private String webSite;
    private boolean coworking;
    private boolean vegan;
    private boolean petFriendly;
    private BigDecimal averageBill;
    private List<String> images;
    private List<String> cuisineNames;
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
