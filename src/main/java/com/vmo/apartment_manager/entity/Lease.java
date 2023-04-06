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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lease extends BaseEntity{

  private double priceApartment;

  private Date startDate;

  private Date endDate;

  private Long idPerson;

  private Long idUser;

  private Boolean status;

  @OneToMany(mappedBy = "lease", cascade = CascadeType.ALL)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Bill> bills;

  @OneToMany(mappedBy = "", cascade = CascadeType.ALL)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Person> persons;

  @ManyToOne
  @JoinColumn(name = "apartment_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Apartment apartment;
}
