package com.expensemanager.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "expenses")
public class Expense {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable=false) private LocalDate expenseDate;
  @Column(nullable=false, precision=14, scale=2) private BigDecimal amount;
  @Column(nullable=false) private String vendorName;
  @Column(length=1000) private String description;
  @Column(nullable=false) private String category;
  @Column(nullable=false) private boolean anomaly;
  @Column(nullable=false) private OffsetDateTime createdAt;

  @PrePersist void created() { createdAt = OffsetDateTime.now(); }

  public Long getId(){return id;}
  public LocalDate getExpenseDate(){return expenseDate;}
  public void setExpenseDate(LocalDate v){expenseDate=v;}
  public BigDecimal getAmount(){return amount;}
  public void setAmount(BigDecimal v){amount=v;}
  public String getVendorName(){return vendorName;}
  public void setVendorName(String v){vendorName=v;}
  public String getDescription(){return description;}
  public void setDescription(String v){description=v;}
  public String getCategory(){return category;}
  public void setCategory(String v){category=v;}
  public boolean isAnomaly(){return anomaly;}
  public void setAnomaly(boolean v){anomaly=v;}
}
