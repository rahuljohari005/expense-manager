package com.expensemanager.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public record ExpenseResponse(Long id, LocalDate date, BigDecimal amount, String vendorName,
                              String description, String category, boolean anomaly) {}
