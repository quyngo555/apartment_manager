package com.vmo.apartment_manager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
  @ManyToOne
  @JoinColumn(name = "bill_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Bill bill;

  public void caculateFee(){
    this.fee = (nextNum - previousNum) * price;
  }
}
