package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.ServiceDto;
import com.vmo.apartment_manager.entity.ServiceDetail;
import java.util.List;

public interface ServiceDetailService {

  ServiceDto add(long billId, ServiceDetail service);
  ServiceDto update(Long id, ServiceDetail serviceDetail);
  ServiceDto findById(long id);
  List<ServiceDto> getAll(Integer pageNo, Integer pageSize, String sortBy);
}
