package io.github.batchuka.hermes.contracts.controller;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ContractRequest(
		@NotBlank String farmer,
		@NotNull @Positive BigDecimal amount,
		@NotNull @Positive Integer termMonths,
		@NotNull @Positive BigDecimal monthlyRate) {
}