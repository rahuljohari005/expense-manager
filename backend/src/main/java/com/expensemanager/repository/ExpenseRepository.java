package com.expensemanager.repository;

import com.expensemanager.entity.Expense;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
  @Query("select avg(e.amount) from Expense e where e.category=:category")
  BigDecimal averageByCategory(@Param("category") String category);

  @Query("select e from Expense e order by e.expenseDate desc, e.id desc")
  List<Expense> findAllOrdered();

  @Query("select e.category, sum(e.amount) from Expense e where e.expenseDate between :start and :end group by e.category order by sum(e.amount) desc")
  List<Object[]> monthlyCategoryTotals(@Param("start") LocalDate start,@Param("end") LocalDate end);

  @Query("select e.vendorName, sum(e.amount) from Expense e where e.expenseDate between :start and :end group by e.vendorName order by sum(e.amount) desc")
  List<Object[]> topVendors(@Param("start") LocalDate start,@Param("end") LocalDate end);

  @Query("select sum(e.amount) from Expense e where e.expenseDate between :start and :end")
  BigDecimal monthlyTotal(@Param("start") LocalDate start,@Param("end") LocalDate end);

  long countByAnomalyTrue();
  List<Expense> findByAnomalyTrueOrderByExpenseDateDescIdDesc();
}
