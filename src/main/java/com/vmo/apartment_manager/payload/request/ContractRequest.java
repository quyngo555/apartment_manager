package com.vmo.apartment_manager.payload.request;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
