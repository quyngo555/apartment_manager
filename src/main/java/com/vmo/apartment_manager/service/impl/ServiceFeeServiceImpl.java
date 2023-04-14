package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.ServiceFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFeeServiceImpl implements ServiceFeeService {
  @Autowired
  ServiceFeeRepository serviceFeeRepo;

  @Override
  public ServiceFee add(ServiceFee serviceFee) {
    return serviceFeeRepo.save(serviceFee);
  }

  @Override
  public ServiceFee update(long id, ServiceFee serviceFee) {
    ServiceFee serviceFee1 = serviceFeeRepo.findById(id).get();
    serviceFee1.setPrice(serviceFee.getPrice());
    return serviceFeeRepo.save(serviceFee1);
  }
}
