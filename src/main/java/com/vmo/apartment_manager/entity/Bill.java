package com.vmo.apartment_manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bill extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
