package com.example.cafehub.dto.cafe;

import java.math.BigDecimal;

public record CafeSearchParametersDto(
        String[] languages,
        boolean coworking,
        boolean vegan,
        boolean petFriendly,
        BigDecimal averageBill,
        boolean childZone,
        boolean smokingFriendly
) {
}
