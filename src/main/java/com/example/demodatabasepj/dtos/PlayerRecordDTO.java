package com.example.demodatabasepj.dtos;

import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PlayerRecordDTO(@NotBlank @NotNull String name, LocalDate birthdate, Position position, Foot foot,
                              double height, @NotNull @PositiveOrZero BigDecimal marketValue, String nacionality) {

}
