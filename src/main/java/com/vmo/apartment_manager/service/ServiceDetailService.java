package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.ServiceDetail;
import java.util.List;

public interface ServiceDetailService {

  ServiceDetail add(ServiceDetail service);
  ServiceDetail update(Long id, ServiceDetail serviceDetail);
  ServiceDetail findById(long id);
  List<ServiceDetail> getAll();
}
