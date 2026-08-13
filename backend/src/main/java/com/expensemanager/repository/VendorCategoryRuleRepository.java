package com.expensemanager.repository;
import com.expensemanager.entity.VendorCategoryRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VendorCategoryRuleRepository extends JpaRepository<VendorCategoryRule,Long> {
  List<VendorCategoryRule> findAllByOrderByVendorPatternAsc();
}
