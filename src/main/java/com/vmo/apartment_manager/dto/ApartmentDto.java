package com.vmo.apartment_manager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentDto {
  private Long id;
  private String ownerApartmentName;
  private Double area;
  private int status;
  private int personInApartment;
  private String apartmentName;

}
