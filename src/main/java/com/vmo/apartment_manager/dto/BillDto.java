package com.vmo.apartment_manager.dto;

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
}
