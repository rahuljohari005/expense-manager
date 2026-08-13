package com.expensemanager.config;

import com.expensemanager.entity.VendorCategoryRule;
import com.expensemanager.repository.VendorCategoryRuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedData {
  @Bean
  CommandLineRunner seed(VendorCategoryRuleRepository vendorCategoryRuleRepository) {
    return args -> {
      if (vendorCategoryRuleRepository.count() > 0) {
        return;
      }

      add(vendorCategoryRuleRepository, "swiggy", "Food");
      add(vendorCategoryRuleRepository, "zomato", "Food");
      add(vendorCategoryRuleRepository, "uber", "Transport");
      add(vendorCategoryRuleRepository, "ola", "Transport");
      add(vendorCategoryRuleRepository, "amazon", "Shopping");
      add(vendorCategoryRuleRepository, "flipkart", "Shopping");
      add(vendorCategoryRuleRepository, "netflix", "Entertainment");
    };
  }

  private void add(VendorCategoryRuleRepository vendorCategoryRuleRepository, String vendorPattern, String category) {
    VendorCategoryRule rule = new VendorCategoryRule();
    rule.setVendorPattern(vendorPattern);
    rule.setCategory(category);
    vendorCategoryRuleRepository.save(rule);
  }
}

