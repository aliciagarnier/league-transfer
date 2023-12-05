package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;
import java.time.LocalDate;

public record MatchRecordDTO(@NotNull UUID leagueID,
                            @NotNull UUID hostTeamID,
                            @NotNull UUID guestTeamID,
                            @PositiveOrZero Integer hostTeamGoals,
                             @PositiveOrZero Integer guestTeamGoals,
                            @NotNull LocalDate date) {
}
