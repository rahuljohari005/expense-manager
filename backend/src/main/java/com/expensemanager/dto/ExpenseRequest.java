package com.expensemanager.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
  @NotNull LocalDate date,
  @NotNull @DecimalMin("0.01") BigDecimal amount,
  @NotBlank String vendorName,
  @Size(max=1000) String description
) {}
