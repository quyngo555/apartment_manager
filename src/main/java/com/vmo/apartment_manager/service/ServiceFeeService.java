package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.ServiceFee;

public interface ServiceFeeService {
  ServiceFee add(ServiceFee serviceFee);
  ServiceFee update(long id, ServiceFee serviceFee);

}
