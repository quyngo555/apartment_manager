package com.vmo.apartment_manager.dto;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ServiceDetail;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDto {
  private Long id;
  private Double total;
  private int stauts;
  private Date dateOfPayment;
  private Double feeRentApartment;
  private List<ServiceDto> serviceDetailList;

  public BillDto(Bill bill) {
    this.id = bill.getId();
    this.total = bill.getTotal();
    this.stauts = bill.getStauts();
    this.dateOfPayment = bill.getDateOfPayment();
    this.feeRentApartment = bill.getContract().getPriceApartment();
  }

  public BillDto(Bill bill, List<ServiceDto> serviceDetailList) {
    this.id = bill.getId();
    this.total = bill.getTotal();
    this.stauts = bill.getStauts();
    this.dateOfPayment = bill.getDateOfPayment();
    this.feeRentApartment = bill.getContract().getPriceApartment();
    this.serviceDetailList = serviceDetailList;
  }
}
