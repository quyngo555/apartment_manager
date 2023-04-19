package com.vmo.apartment_manager.payload.request;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRequest {
  private double priceApartment;
  private Date startDate;
  private Date endDate;
  private String code;
  private ContractStatus status;
  private Apartment apartment;
  private Person person;
  private Boolean liveHere;


}
