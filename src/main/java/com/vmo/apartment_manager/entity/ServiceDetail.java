package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
public class ServiceDetail extends BaseEntity {
  private Double price;
  private String unit;
  private int nextNum;
  private int previousNum;
  private String note;
  private  TypeService name;
  private Double fee;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bill_id")
  private Bill bill;
}
