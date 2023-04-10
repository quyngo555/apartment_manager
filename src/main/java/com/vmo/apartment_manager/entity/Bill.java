package com.vmo.apartment_manager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.sql.Date;
import java.util.List;
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
@AllArgsConstructor
@NoArgsConstructor
public class Bill extends BaseEntity {

  private Double total;
  private int stauts;
  private Date dateOfPayment;

  @ManyToOne
  @JoinColumn(name = "contract_id")
  private Contract contract;

  @OneToMany(mappedBy = "bill")
  List<ServiceDetail> serviceDetails;
}
