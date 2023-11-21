package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record LeagueRecordDTO(@NotBlank String name, @NotBlank String country,
                              @NotBlank String region, BigDecimal marketValue) {
}
