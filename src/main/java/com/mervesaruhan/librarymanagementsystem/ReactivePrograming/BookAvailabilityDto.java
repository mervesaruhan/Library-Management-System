package com.mervesaruhan.librarymanagementsystem.ReactivePrograming;

import java.time.LocalDate;

public record BookAvailabilityDto(
        Long id,
        String title,
        Integer inventoryCount
) {
}
