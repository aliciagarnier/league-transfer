package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotBlank;


public record LeagueRecordDTO(@NotBlank String name, @NotBlank String country,
                              @NotBlank String region) {
}
