package com.vmo.apartment_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Apartment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Name is mandatory")
  @Column(unique=true)
  private String name;

  @Column(unique=true)
  private String code;

  @NotNull(message = "Area is mandatory")
  private Double area;

  private Integer status;

  private String description;
}
