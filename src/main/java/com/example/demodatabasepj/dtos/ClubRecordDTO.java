package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ClubRecordDTO(@NotBlank String name, String stadium, BigDecimal marketValue) {
}
