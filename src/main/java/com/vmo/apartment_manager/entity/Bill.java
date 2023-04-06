package com.vmo.apartment_manager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill extends BaseEntity {

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "bill_service_detail",
      joinColumns = @JoinColumn(name = "bill_id"),
      inverseJoinColumns = @JoinColumn(name = "service_detail_id"))
  @ToString.Exclude
  private Set<ServiceDetail> serviceDetails;

  @ManyToOne()
  @JoinColumn(name = "lease_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Lease lease;

}
