package com.example.cafehub.dto.cafe;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
    private String workHoursWeekDays;
    private String workHoursWeekends;
    private String urlOfImage;
    private String webSite;
    private boolean coworking;
    private boolean vegan;
    private boolean petFriendly;
    private BigDecimal averageBill;
    private List<String> images;
    private boolean childZone;
    private boolean smokingFriendly;
}
