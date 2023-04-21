package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.ServiceFeeService;
import java.util.List;
import java.util.Optional;
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
    Optional<ServiceFee> serviceFee1 = serviceFeeRepo.findById(id);
    if(serviceFee1.isPresent())
      serviceFee1.get().setPrice(serviceFee.getPrice());
    return serviceFeeRepo.save(serviceFee1.get());
  }

  @Override
  public List<ServiceFee> getAll() {
    return serviceFeeRepo.findAll();
  }

  @Override
  public Optional<ServiceFee> findById(long id) {
    return serviceFeeRepo.findById(id);
  }

  @Override
  public void deleteById(long id) {
    serviceFeeRepo.deleteById(id);
  }

}
