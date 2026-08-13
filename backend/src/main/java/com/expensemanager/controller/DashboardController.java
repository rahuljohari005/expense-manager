package com.expensemanager.controller;

import com.expensemanager.dto.DashboardDtos.Dashboard;
import com.expensemanager.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
public class DashboardController {
  private final DashboardService service;

  public DashboardController(DashboardService service) {
    this.service = service;
  }

  @GetMapping
  public Dashboard dashboard(@RequestParam(required = false) String month) {
    return service.dashboard(month == null || month.isBlank() ? YearMonth.now() : YearMonth.parse(month));
  }
}
