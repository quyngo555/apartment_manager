package com.vmo.apartment_manager.payload.request;

import com.vmo.apartment_manager.entity.BillDetail;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest {

  private Date paidDate;
  private String note;
  private Long apartmentId;
  List<BillDetail> billDetailList;
}
