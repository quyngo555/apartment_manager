package com.vmo.apartment_manager.dto;

import com.vmo.apartment_manager.entity.ServiceDetail;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillRequest {
  private Date dateOfPayment;
  private Long apartmentId;
}
