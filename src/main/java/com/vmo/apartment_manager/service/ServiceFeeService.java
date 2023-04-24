package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.payload.response.ServiceFeeResponse;
import java.util.List;
import java.util.Optional;

public interface ServiceFeeService {
  ServiceFeeResponse add(ServiceFee serviceFee);
  ServiceFeeResponse update(long id, ServiceFee serviceFee);
  List<ServiceFee> getAll();
  ServiceFeeResponse findById(long id);
  void deleteById(long id);

}
