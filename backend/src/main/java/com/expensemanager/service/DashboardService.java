package com.expensemanager.service;

import com.expensemanager.dto.*;
import com.expensemanager.dto.DashboardDtos.*;
import com.expensemanager.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@Service
public class DashboardService {
  private final ExpenseRepository repo;
  public DashboardService(ExpenseRepository repo){this.repo=repo;}

  public Dashboard dashboard(YearMonth month){
    var start=month.atDay(1); var end=month.atEndOfMonth();
    var categories=repo.monthlyCategoryTotals(start,end).stream()
      .map(r->new MonthlyCategory((String)r[0],(BigDecimal)r[1])).toList();
    var vendors=repo.topVendors(start,end).stream().limit(5)
      .map(r->new VendorSpend((String)r[0],(BigDecimal)r[1])).toList();
    var anomalies=repo.findByAnomalyTrueOrderByExpenseDateDescIdDesc().stream()
      .map(e->new ExpenseResponse(e.getId(),e.getExpenseDate(),e.getAmount(),e.getVendorName(),
        e.getDescription(),e.getCategory(),e.isAnomaly())).toList();
    return new Dashboard(month.toString(),Optional.ofNullable(repo.monthlyTotal(start,end)).orElse(BigDecimal.ZERO),
      categories,vendors,repo.countByAnomalyTrue(),anomalies);
  }
}
