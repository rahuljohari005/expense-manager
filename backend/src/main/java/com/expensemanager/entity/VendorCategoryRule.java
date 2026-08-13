package com.expensemanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name="vendor_category_rules")
public class VendorCategoryRule {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String vendorPattern;
  @Column(nullable=false) private String category;
  public Long getId(){return id;}
  public String getVendorPattern(){return vendorPattern;}
  public void setVendorPattern(String v){vendorPattern=v;}
  public String getCategory(){return category;}
  public void setCategory(String v){category=v;}
}
