package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.payload.request.BillRequest;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.payload.response.BillResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;

import com.vmo.apartment_manager.payload.response.ContractResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BillService {
  Bill add(BillRequest bill);
  BillResponse update(long id, Bill bill);
  BillResponse findById(long id);
  Page<BillResponse> findAll(Integer pageNo,Integer pageSize, String sortBy);
  boolean hasExcelFormat(MultipartFile file);
  String importExcel(InputStream is);
  ByteArrayInputStream exportExcel(Date startDate, Date endDate);

  Page<BillResponse> findBillByCreatedBetween(Date startDate, Date endDate, String apartmentCode, Integer pageNo, Integer pageSize, String sortBy);

}
