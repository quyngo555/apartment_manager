package com.vmo.apartment_manager.payload.response;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractResponse {
  private long id;
  private double priceApartment;
  private Date startDate;
  private String createdBy;
  private String modifiedBy;
  private Date endDate;
  private String code;
  private ContractStatus status;
  private Apartment apartment;
  private PersonResponse person;

  public ContractResponse(Contract contract) {
    this.id = contract.getId();
    this.priceApartment = contract.getPriceApartment();
    this.startDate = contract.getStartDate();
    this.endDate = contract.getEndDate();
    this.code = contract.getCode();
    this.status = contract.getStatus();
    this.apartment = contract.getApartment();
    this.person = new PersonResponse(contract.getPerson());
    this.createdBy = contract.getCreatedBy();
    this.modifiedBy = contract.getModifiedBy();
  }
}
