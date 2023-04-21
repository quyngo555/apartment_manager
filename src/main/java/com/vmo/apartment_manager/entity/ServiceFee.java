package com.vmo.apartment_manager.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFee extends BaseEntity {
  @NotNull(message = "Price is mandatory")
  private Double price;

  @NotNull(message = "Unit is mandatory")
  private String unit;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Name is mandatory")
  private  TypeService name;
}

