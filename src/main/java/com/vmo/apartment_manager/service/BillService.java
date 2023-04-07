package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Bill;
import java.util.List;

public interface BillService {
  Bill add(Bill bill);
  Bill update(Long id, Bill bill);
  Bill findById(long id);
  List<Bill> getAllBill();

}
