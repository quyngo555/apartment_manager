package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ServiceDetailRepository;
import com.vmo.apartment_manager.service.ServiceDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDetailServiceImpl implements ServiceDetailService {

  @Autowired
  ServiceDetailRepository serviceDetailRepo;

  @Override
  public ServiceDetail add(ServiceDetail service) {
    return serviceDetailRepo.save(service);
  }

  @Override
  public ServiceDetail update(Long id, ServiceDetail serviceDetail) {
    ServiceDetail serviceDetail1 = serviceDetailRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.SERVICE_NOT_FOUND + id);
    });
    serviceDetail1.setName(serviceDetail.getName());
    serviceDetail1.setNote(serviceDetail.getNote());
    serviceDetail1.setUnit(serviceDetail.getUnit());
    serviceDetail1.setPrice(serviceDetail.getPrice());
    serviceDetail1.setBill(serviceDetail.getBill());
    serviceDetail1 = serviceDetailRepo.save(serviceDetail1);
    return serviceDetail1;
  }

  @Override
  public ServiceDetail findById(long id) {
    return serviceDetailRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.SERVICE_NOT_FOUND + id);
    });
  }

  @Override
  public List<ServiceDetail> getAll() {
    return serviceDetailRepo.findAll();
  }
}
