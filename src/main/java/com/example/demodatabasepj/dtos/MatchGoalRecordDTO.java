package com.example.demodatabasepj.dtos;

import com.example.demodatabasepj.enumerator.GoalType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MatchGoalRecordDTO(@NotNull UUID match_id,
                                 @NotNull UUID player_id,
                                 @NotNull UUID club_id,
                                 @NotNull GoalType goalType) {
}
