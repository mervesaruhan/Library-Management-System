package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import com.mervesaruhan.librarymanagementsystem.model.enums.AvailabilityEnum;
import jakarta.validation.constraints.NotNull;

public record BookAvailabilityUpdateDto(

        @NotNull(message =  "Müsaitlik durumu boş olamaz.")
        AvailabilityEnum availability
) {
}
