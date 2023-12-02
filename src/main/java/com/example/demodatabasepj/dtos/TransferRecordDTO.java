package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Getter
public record TransferRecordDTO(UUID player_id,
                                @NotNull UUID club_left_id,
                                @NotNull  UUID club_join_id,
                                @NotNull LocalDate date,
                                BigDecimal fee) {
}
