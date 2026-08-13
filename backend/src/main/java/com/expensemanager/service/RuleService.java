package com.expensemanager.service;

import com.expensemanager.dto.*;
import com.expensemanager.entity.VendorCategoryRule;
import com.expensemanager.repository.VendorCategoryRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RuleService {
  private final VendorCategoryRuleRepository repo;
  public RuleService(VendorCategoryRuleRepository repo){this.repo=repo;}

  public List<RuleResponse> findAll(){
    return repo.findAllByOrderByVendorPatternAsc().stream()
      .map(r->new RuleResponse(r.getId(),r.getVendorPattern(),r.getCategory())).toList();
  }
  @Transactional public RuleResponse create(RuleRequest r){
    VendorCategoryRule x=new VendorCategoryRule();
    x.setVendorPattern(r.vendorPattern().trim()); x.setCategory(r.category().trim());
    x=repo.save(x); return new RuleResponse(x.getId(),x.getVendorPattern(),x.getCategory());
  }
  @Transactional public RuleResponse update(Long id,RuleRequest r){
    VendorCategoryRule x=repo.findById(id).orElseThrow();
    x.setVendorPattern(r.vendorPattern().trim()); x.setCategory(r.category().trim());
    return new RuleResponse(x.getId(),x.getVendorPattern(),x.getCategory());
  }
  public void delete(Long id){repo.deleteById(id);}
}
