package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.response.ServiceFeeResponse;
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
  public ServiceFeeResponse add(ServiceFee serviceFee) {
    return new ServiceFeeResponse(serviceFeeRepo.save(serviceFee));
  }

  @Override
  public ServiceFeeResponse update(long id, ServiceFee serviceFee) {
    ServiceFee serviceFeeOld = serviceFeeRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.SERVICE_NOT_EXISTS + id);
    });
    serviceFee.setId(id);
    return  new ServiceFeeResponse(serviceFeeRepo.save(serviceFee));
  }

  @Override
  public List<ServiceFee> getAll() {
    return serviceFeeRepo.findAll();
  }

  @Override
  public ServiceFeeResponse findById(long id) {
    ServiceFee serviceFee = serviceFeeRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.SERVICE_NOT_EXISTS + id);
    });
    return new ServiceFeeResponse(serviceFee);
  }

  @Override
  public void deleteById(long id) {
    ServiceFee serviceFee = serviceFeeRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.SERVICE_NOT_EXISTS  + id);
    });
    serviceFeeRepo.delete(serviceFee);
  }

}
