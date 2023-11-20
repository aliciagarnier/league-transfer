package com.example.demodatabasepj.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlayerRecordDTO(@NotBlank @NotNull String name){
}
