package com.example.demodatabasepj.dtos;

import com.example.demodatabasepj.models.Club;



import com.example.demodatabasepj.models.Player;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransferRecordDTO(UUID player_id,
                                UUID club_left_id,
                                @NotNull  UUID club_join_id,
                                @NotNull LocalDate date,
                                BigDecimal fee) {
}
