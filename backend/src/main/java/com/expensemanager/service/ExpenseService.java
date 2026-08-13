package com.expensemanager.service;

import com.expensemanager.dto.*;
import com.expensemanager.entity.*;
import com.expensemanager.repository.*;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExpenseService {
  private final ExpenseRepository expenses;
  private final VendorCategoryRuleRepository rules;

  public ExpenseService(ExpenseRepository expenses, VendorCategoryRuleRepository rules){
    this.expenses=expenses; this.rules=rules;
  }

  @Transactional
  public ExpenseResponse create(ExpenseRequest r){
    String category=categorize(r.vendorName());
    BigDecimal avg=expenses.averageByCategory(category);
    boolean anomaly=avg!=null && avg.compareTo(BigDecimal.ZERO)>0 &&
        r.amount().compareTo(avg.multiply(BigDecimal.valueOf(3)))>0;

    Expense e=new Expense();
    e.setExpenseDate(r.date()); e.setAmount(r.amount());
    e.setVendorName(r.vendorName().trim());
    e.setDescription(r.description()==null ? "" : r.description().trim());
    e.setCategory(category); e.setAnomaly(anomaly);
    return response(expenses.save(e));
  }

  public List<ExpenseResponse> findAll(){
    return expenses.findAllOrdered().stream().map(this::response).toList();
  }

  public String categorize(String vendor){
    String v=vendor.toLowerCase(Locale.ROOT);
    return rules.findAll().stream()
      .filter(r->v.contains(r.getVendorPattern().toLowerCase(Locale.ROOT)))
      .map(VendorCategoryRule::getCategory).findFirst().orElse("Other");
  }

  @Transactional
  public ImportResult importCsv(MultipartFile file) throws IOException {
    if(file.isEmpty()) throw new IllegalArgumentException("CSV file is empty");
    int imported=0; List<String> errors=new ArrayList<>();
    try(Reader reader=new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        CSVParser parser=CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
          .setIgnoreHeaderCase(true).setTrim(true).build().parse(reader)){
      for(CSVRecord row:parser){
        try{
          if(!row.isMapped("date")||row.get("date").isBlank()) throw new IllegalArgumentException("Missing date");
          if(!row.isMapped("amount")||row.get("amount").isBlank()) throw new IllegalArgumentException("Missing amount");
          if(!row.isMapped("vendorName")||row.get("vendorName").isBlank()) throw new IllegalArgumentException("Missing vendorName");
          create(new ExpenseRequest(LocalDate.parse(row.get("date")),
            new BigDecimal(row.get("amount")),row.get("vendorName"),
            row.isMapped("description")?row.get("description"):""));
          imported++;
        }catch(Exception ex){errors.add("Row "+row.getRecordNumber()+": "+ex.getMessage());}
      }
    }
    return new ImportResult(imported,errors);
  }

  private ExpenseResponse response(Expense e){
    return new ExpenseResponse(e.getId(),e.getExpenseDate(),e.getAmount(),e.getVendorName(),
      e.getDescription(),e.getCategory(),e.isAnomaly());
  }
  public record ImportResult(int imported,List<String> errors){}
}
