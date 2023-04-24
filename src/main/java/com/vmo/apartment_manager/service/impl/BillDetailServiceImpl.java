package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.response.BillDetailResponse;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.BillDetailService;
import com.vmo.apartment_manager.service.BillService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillDetailServiceImpl implements BillDetailService {

  @Autowired
  BillDetailRepository billDetailRepo;

  @Autowired
  ServiceFeeRepository serviceFeeRepo;

  @Autowired
  BillRepository billRepo;
  @Override
  public BillDetailResponse add(BillDetail billDetail) {
    ServiceFee serviceFee = serviceFeeRepo.findById(billDetail.getServiceFee().getId()).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.SERVICE_NOT_EXISTS + billDetail.getServiceFee().getId());
    });
    Bill bill = billRepo.findById(billDetail.getBill().getId()).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + billDetail.getBill().getId());
    });
    billDetail.setSubTotal(billDetail.getConsume() * serviceFee.getPrice());
    billDetail.setServiceFee(serviceFee);
    billDetail.setBill(bill);
    billDetailRepo.save(billDetail);
    bill.setTotal(bill.getTotal() + billDetail.getSubTotal());
    billRepo.save(bill);
    return new BillDetailResponse(billDetail);
  }

  @Override
  public BillDetailResponse update(long id, BillDetail billDetail) {
    BillDetail billDetail1 = billDetailRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    });
    billDetail.setId(id);
    billDetail.setSubTotal(billDetail.getConsume() * billDetail.getServiceFee().getPrice());
    Bill bill = billRepo.findById(billDetail1.getBill().getId()).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + billDetail.getBill().getId());
    });
    billDetailRepo.save(billDetail);

    bill.setTotal(bill.getTotal() + billDetail1.getSubTotal());
    billRepo.save(bill);
    return new BillDetailResponse(billDetail);
  }



  @Override
  public BillDetailResponse findById(long id) {
    BillDetail billDetail = billDetailRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.BILL_DETAIL_NOT_FOUND + id);
    });
    return new BillDetailResponse(billDetail);
  }

  @Override
  public List<BillDetailResponse> getAll() {
    return billDetailRepo.findAll().stream()
        .map(BillDetailResponse::new)
        .toList();
  }



}
