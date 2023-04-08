package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.BillDto;
import com.vmo.apartment_manager.entity.Bill;
import java.util.List;

public interface BillService {
  BillDto add(Bill bill);
  BillDto update(Long id, Bill bill);
  BillDto findById(long id);
  List<BillDto> getAllBill(Integer pageNo, Integer pageSize, String sortBy);
  Double getTotalFee(Bill bill);
  List<BillDto> getBillsByApartmentId(long apartmentId);

}
