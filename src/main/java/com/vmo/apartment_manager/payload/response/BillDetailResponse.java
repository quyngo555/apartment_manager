package com.vmo.apartment_manager.payload.response;

import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.ServiceFee;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BillDetailResponse {
  private Double subTotal;
  private Double consume;
  private ServiceFeeResponse serviceFee;

  public BillDetailResponse(BillDetail billDetail) {
    this.subTotal = billDetail.getSubTotal();
    this.consume = billDetail.getConsume();
    this.serviceFee = new ServiceFeeResponse(billDetail.getServiceFee());
  }
}
