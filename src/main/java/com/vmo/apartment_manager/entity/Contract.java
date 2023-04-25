package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Contract extends BaseEntity{


  @NotNull(message = "Price apartment is mandatory")
  private double priceApartment;

  @NotNull(message = "startDate is mandatory")
  private Date startDate;

  @NotNull(message = "endDate is mandatory")
  private Date endDate;

  @NotNull(message = "Code is mandatory")
  private String code;

  @Enumerated(EnumType.STRING)
  private ContractStatus status;

  @NotNull(message = "Apartment is mandatory")
  @ManyToOne
  @JoinColumn(name = "apartment_id")
  private Apartment apartment;

  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;
}
