package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.payload.request.BillRequest;
import com.vmo.apartment_manager.entity.Bill;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface BillService {
  Bill add(BillRequest bill);
  Bill update(long id, Bill bill);
  Bill findById(long id);
  List<Bill> findAll();
  boolean hasExcelFormat(MultipartFile file);
  void importExcel(InputStream is);

}
