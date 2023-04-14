package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.BillDetail;
import java.util.List;

public interface BillDetailService {
  BillDetail add(BillDetail billDetail);
  BillDetail update(long id, BillDetail billDetail);
  BillDetail findById(long id);
  List<BillDetail> getAll();
}
