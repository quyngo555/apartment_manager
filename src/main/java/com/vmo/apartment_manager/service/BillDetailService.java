package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.payload.response.BillDetailResponse;
import java.util.List;

public interface BillDetailService {
  BillDetailResponse add(BillDetail billDetail);
  BillDetailResponse update(long id, BillDetail billDetail);
  BillDetailResponse findById(long id);
  List<BillDetailResponse> getAll();
}
