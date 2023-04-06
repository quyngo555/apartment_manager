package com.vmo.apartment_manager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Date;
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
public class Contract extends BaseEntity{

  private double priceApartment;

  private Date startDate;

  private Date endDate;

  private Long idPerson;

  private Long idUser;

  private Integer status;

  @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Bill> bills;

  @ManyToOne
  @JoinColumn(name = "person_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Person persons;
}
