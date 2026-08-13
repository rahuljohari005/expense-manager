package com.expensemanager.dto;
import java.math.BigDecimal;
import java.util.List;

public final class DashboardDtos {
  private DashboardDtos(){}
  public record MonthlyCategory(String category, BigDecimal total){}
  public record VendorSpend(String vendorName, BigDecimal total){}
  public record Dashboard(String month, BigDecimal monthlyTotal, List<MonthlyCategory> categoryTotals,
                          List<VendorSpend> topVendors, long anomalyCount, List<ExpenseResponse> anomalies){}
}
