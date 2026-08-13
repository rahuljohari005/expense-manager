package com.expensemanager.controller;

import com.expensemanager.dto.*;
import com.expensemanager.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
public class ExpenseController {
  private final ExpenseService service;

  public ExpenseController(ExpenseService service) {
    this.service = service;
  }

  @PostMapping
  public ExpenseResponse create(@Valid @RequestBody ExpenseRequest r) {
    return service.create(r);
  }

  @GetMapping
  public List<ExpenseResponse> findAll() {
    return service.findAll();
  }

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ExpenseService.ImportResult importCsv(@RequestParam("file") MultipartFile file) throws IOException {
    return service.importCsv(file);
  }
}
