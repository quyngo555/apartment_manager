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

  public ApartmentResponse(Apartment apartment, String roomMaster, String contractCode, int personInApartment) {
    this.id = apartment.getId();
    this.roomMaster = roomMaster;
    this.contractCode = contractCode;
    this.status = apartment.getStatus();
    this.personInApartment = personInApartment;
    this.apartmentCode = apartment.getCode();
    this.area = apartment.getArea();
  }

  public ApartmentResponse(Apartment apartment) {
    this.id = apartment.getId();
    this.apartmentCode = apartment.getCode();
    this.status = apartment.getStatus();
    this.area = apartment.getArea();
  }
}
