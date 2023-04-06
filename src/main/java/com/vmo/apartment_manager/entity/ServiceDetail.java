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
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetail extends BaseEntity {
  private Double price;
  private String unit;
  private String note;
  private  TypeService name;
  @ManyToMany(mappedBy = "serviceDetails")
  private Set<Bill> bills;
}
