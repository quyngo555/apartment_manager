package com.vmo.apartment_manager.dto;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.Contract;
import java.sql.Date;
import java.util.List;

public class BillDto {
  private Double total;
  private int stauts;
  private Date paidDate;
  private String note;
  private List<BillDetail> billDetails;
  private String  apartmentName;

  public BillDto(Bill bill) {
    this.total = bill.getTotal();
    this.stauts = bill.getStauts();
    this.paidDate = bill.getPaidDate();
    this.note = bill.getNote();
//    this.billDetails = bill.getBillDetailList().stream().map(BillDetail :: new).toList();
    this.apartmentName = bill.getContract().getApartment().getName();
  }
}
