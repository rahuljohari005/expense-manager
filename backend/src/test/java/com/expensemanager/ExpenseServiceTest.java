package com.expensemanager;
import com.expensemanager.dto.ExpenseRequest;
import com.expensemanager.entity.Expense;
import com.expensemanager.entity.VendorCategoryRule;
import com.expensemanager.repository.*;
import com.expensemanager.service.ExpenseService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {
  @Test void categorizesVendorCaseInsensitively(){
    var expenses=mock(ExpenseRepository.class); var rules=mock(VendorCategoryRuleRepository.class);
    var rule=new VendorCategoryRule(); rule.setVendorPattern("swiggy");rule.setCategory("Food");
    when(rules.findAll()).thenReturn(List.of(rule));
    var service=new ExpenseService(expenses,rules);
    assertEquals("Food",service.categorize("SWIGGY ORDER"));
    assertEquals("Other",service.categorize("Unknown"));
  }
  @Test void flagsMoreThanThreeTimesAverage(){
    var expenses=mock(ExpenseRepository.class); var rules=mock(VendorCategoryRuleRepository.class);
    var rule=new VendorCategoryRule(); rule.setVendorPattern("swiggy");rule.setCategory("Food");
    when(rules.findAll()).thenReturn(List.of(rule));
    when(expenses.averageByCategory("Food")).thenReturn(new BigDecimal("100"));
    when(expenses.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));
    var service=new ExpenseService(expenses,rules);
    var result=service.create(new ExpenseRequest(LocalDate.now(),new BigDecimal("301"),"Swiggy","Large order"));
    assertTrue(result.anomaly());
  }
}
