package com.vmo.apartment_manager.payload.response;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillResponse implements Serializable {
  private Double total;
  private Boolean stauts;
  private Date paidDate;
  private String note;
  private Date termPayment;
  private List<BillDetailResponse> billDetails;
  private String  apartmentName;
  private String createdBy;

  public BillResponse(Bill bill) {
    this.createdBy = bill.getCreatedBy();
    this.total = bill.getTotal();
    this.stauts = bill.getStauts();
    this.paidDate = bill.getPaidDate();
    this.note = bill.getNote();
    this.termPayment = bill.getTermPayment();
    this.billDetails = bill.getBillDetailList().stream().map(BillDetailResponse::new).toList();
    this.apartmentName = bill.getContract().getApartment().getName();
  }
}