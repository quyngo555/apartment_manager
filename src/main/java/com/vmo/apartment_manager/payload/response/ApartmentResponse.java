package com.vmo.apartment_manager.payload.response;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.ContractStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApartmentResponse {
  private Long id;
  private String roomMaster;
  private String contractCode;
  private Boolean status;
  private int personInApartment;
  private String apartmentCode;
  private Double area;


}
