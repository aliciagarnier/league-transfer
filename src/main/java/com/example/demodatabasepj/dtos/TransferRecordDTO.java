package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransferRecordDTO(@NotNull UUID player_id,
                                UUID club_left_id,
                                UUID club_join_id,
                                @NotNull LocalDate date,
                                BigDecimal fee) {
}