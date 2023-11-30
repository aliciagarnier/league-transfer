package com.example.demodatabasepj.dtos;

import com.example.demodatabasepj.models.Club;



import com.example.demodatabasepj.models.Player;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRecordDTO(@NotNull Player player, @NotNull Club join, @NotNull Club left, LocalDate date, @NotNull
                                BigDecimal fee) {
}
