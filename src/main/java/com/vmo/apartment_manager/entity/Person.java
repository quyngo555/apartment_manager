package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person extends BaseEntity{
  private String fullName;
  private Date dob;
  private String phone;
  private String email;
  private String cin;
  private Boolean gender;
  private String carrer;
  private Integer idParent;
  private Integer idChild;

  @ManyToOne
  @JoinColumn(name = "lease_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Lease lease;
}
