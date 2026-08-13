package com.expensemanager.dto;
import jakarta.validation.constraints.NotBlank;
public record RuleRequest(@NotBlank String vendorPattern, @NotBlank String category) {}
