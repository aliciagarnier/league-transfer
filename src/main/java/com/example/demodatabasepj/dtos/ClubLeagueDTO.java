package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ClubLeagueDTO(@NotNull  UUID league_id, @NotNull UUID club_id, @NotNull LocalDate date) {
}
