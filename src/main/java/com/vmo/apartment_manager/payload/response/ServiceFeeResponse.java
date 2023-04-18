package com.vmo.apartment_manager.payload.response;

import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceFeeResponse {
  private Double price;
  private String unit;
  private TypeService name;

  public ServiceFeeResponse(ServiceFee serviceFee) {
    this.price = serviceFee.getPrice();
    this.unit = serviceFee.getUnit();
    this.name = serviceFee.getName();
  }
}
