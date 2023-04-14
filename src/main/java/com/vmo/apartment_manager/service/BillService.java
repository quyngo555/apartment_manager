package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.BillDto;
import com.vmo.apartment_manager.entity.Bill;
import java.util.List;

public interface BillService {
  Bill add(Bill bill);
  Bill update(long id, Bill bill);
  Bill findById(long id);
  List<BillDto> findAll();

}
