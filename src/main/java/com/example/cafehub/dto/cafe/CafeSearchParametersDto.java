package com.example.cafehub.dto.cafe;

import java.math.BigDecimal;

public record CafeSearchParametersDto(
        boolean openNow,
        String[] languages,
        boolean coworking,
        boolean vegan,
        boolean petFriendly,
        BigDecimal averageBill,
        String[] cuisines,
        boolean fastService,
        boolean wifi,
        boolean businessLunch,
        boolean freeWater,
        boolean boardGames,
        boolean birthday,
        boolean businessMeeting,
        boolean childHoliday,
        boolean romantic,
        boolean thematicEvent,
        boolean familyHoliday,
        boolean parking,
        boolean terrace
) {
}
