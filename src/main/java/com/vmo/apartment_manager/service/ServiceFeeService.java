package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.ServiceFee;
import java.util.List;
import java.util.Optional;

public interface ServiceFeeService {
  ServiceFee add(ServiceFee serviceFee);
  ServiceFee update(long id, ServiceFee serviceFee);
  List<ServiceFee> getAll();
  Optional<ServiceFee> findById(long id);
  void deleteById(long id);

}
