package com.vmo.apartment_manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDetail extends BaseEntity {

  private Double subTotal;

  @NotNull(message = "Name is mandatory")
  @Column(unique=true)
  private Double consume;

  @ManyToOne
  @JoinColumn(name = "bill_id")
  @JsonBackReference
  private Bill bill;

  @ManyToOne
  @JoinColumn(name = "service_fee_id")
  private ServiceFee serviceFee;


}
