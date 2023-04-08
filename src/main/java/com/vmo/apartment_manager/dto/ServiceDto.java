package com.vmo.apartment_manager.dto;

import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.entity.TypeService;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDto {
  private Long id;
  private Timestamp createdDate;
  private Timestamp modifiedDate;
  private Double price;
  private String unit;
  private int nextNum;
  private int previousNum;
  private String note;
  private TypeService name;
  private Double fee;
  private BillDto billDto;

  public ServiceDto(ServiceDetail serviceDetail, boolean b) {
    this.id = serviceDetail.getId();
    this.createdDate = serviceDetail.getCreatedDate();
    this.modifiedDate = serviceDetail.getModifiedDate();
    this.price = serviceDetail.getPrice();
    this.unit = serviceDetail.getUnit();
    this.nextNum = serviceDetail.getNextNum();
    this.previousNum = serviceDetail.getPreviousNum();
    this.note = serviceDetail.getNote();
    this.name = serviceDetail.getName();
    this.fee = serviceDetail.getFee();
  }

  public ServiceDto(ServiceDetail serviceDetail) {
    this.id = serviceDetail.getId();
    this.createdDate = serviceDetail.getCreatedDate();
    this.modifiedDate = serviceDetail.getModifiedDate();
    this.price = serviceDetail.getPrice();
    this.unit = serviceDetail.getUnit();
    this.nextNum = serviceDetail.getNextNum();
    this.previousNum = serviceDetail.getPreviousNum();
    this.note = serviceDetail.getNote();
    this.name = serviceDetail.getName();
    this.fee = serviceDetail.getFee();
    this.billDto = new BillDto(serviceDetail.getBill());
  }


}
