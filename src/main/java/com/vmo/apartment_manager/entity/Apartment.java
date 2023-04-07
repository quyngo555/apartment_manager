package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Apartment extends BaseEntity {
  private String name;
  private String code;
  private Double area;
  private Integer status;
  private String description;
}
