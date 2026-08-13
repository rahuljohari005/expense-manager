package com.expensemanager.controller;

import com.expensemanager.dto.*;
import com.expensemanager.service.RuleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
public class RuleController {
  private final RuleService service;

  public RuleController(RuleService service) {
    this.service = service;
  }

  @GetMapping
  public List<RuleResponse> findAll() {
    return service.findAll();
  }

  @PostMapping
  public RuleResponse create(@Valid @RequestBody RuleRequest r) {
    return service.create(r);
  }

  @PutMapping("/{id}")
  public RuleResponse update(@PathVariable Long id, @Valid @RequestBody RuleRequest r) {
    return service.update(id, r);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
