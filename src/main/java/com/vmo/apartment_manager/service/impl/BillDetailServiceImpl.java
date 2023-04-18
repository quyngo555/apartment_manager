package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.BillDetailService;
import com.vmo.apartment_manager.service.BillService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillDetailServiceImpl implements BillDetailService {

  @Autowired
  BillDetailRepository billDetailRepo;

  @Autowired
  ServiceFeeRepository serviceFeeRepo;



  @Override
  public BillDetail add(BillDetail billDetail) {
    ServiceFee serviceFee = serviceFeeRepo.findById(billDetail.getServiceFee().getId()).get();
    billDetail.setSubTotal(billDetail.getConsume() * serviceFee.getPrice());
    BillDetail billDetail1 =  billDetailRepo.save(billDetail);
    return billDetail1;
  }

  @Override
  public BillDetail update(long id, BillDetail billDetail) {
    BillDetail billDetail1 = billDetailRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    });
    billDetail1.setConsume(billDetail.getConsume());
    billDetail1.setSubTotal(billDetail.getConsume() * billDetail.getServiceFee().getPrice());
    billDetail1 =  billDetailRepo.save(billDetail1);
    return billDetail1;
  }

  @Override
  public BillDetail findById(long id) {
    return billDetailRepo.findById(id).get();
  }

  @Override
  public List<BillDetail> getAll() {
    return billDetailRepo.findAll();
  }



}
