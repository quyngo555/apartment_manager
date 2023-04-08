package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.BillDto;
import com.vmo.apartment_manager.entity.Bill;
import java.util.List;

public interface BillService {
  Bill add(Bill bill);
  BillDto update(Long id, Bill bill);
  BillDto findById(long id);
  List<BillDto> getAllBill();
  Double getTotalFee(Bill bill);

}
