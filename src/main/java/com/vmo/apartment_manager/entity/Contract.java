package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
public class Contract extends BaseEntity{

  @NotNull(message = "Price apartment is mandatory")
  private double priceApartment;

  @NotNull(message = "Area is mandatory")
  private Date startDate;

  @NotNull(message = "Area is mandatory")
  private Date endDate;

  @NotNull(message = "Area is mandatory")
  private String code;

  @Enumerated(EnumType.STRING)
  private ContractStatus status;

  @ManyToOne
  @JoinColumn(name = "apartment_id")
  private Apartment apartment;

  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;
}
