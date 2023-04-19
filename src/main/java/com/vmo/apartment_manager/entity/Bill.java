package com.vmo.apartment_manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bill extends BaseEntity {
  private Double total;
  private Boolean stauts;
  private Date paidDate;
  private String note;
  private Date termPayment;
  @ManyToOne
  @JoinColumn(name = "contract_id")
  private Contract contract;

  @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
  @JsonManagedReference
  List<BillDetail> billDetailList;
}
