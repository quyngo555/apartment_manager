package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.service.ApartmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApartmentServiceImpl implements ApartmentService {

  @Autowired
  ApartmentRepository apartmentRepo;

  @Override
  public Apartment add(Apartment apartment) {
    return apartmentRepo.save(apartment);
  }

  @Override
  public Apartment update(Long id, Apartment apartment) {
    Apartment apartment1 = apartmentRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
    apartment1.setStatus(apartment.getStatus());
    apartment1.setName(apartment.getName());
    apartment1.setCode(apartment.getCode());
    apartment1.setArea(apartment.getArea());
    apartment1.setDescription(apartment.getDescription());
    apartment1 = apartmentRepo.save(apartment1);
    return  apartment1;
  }

  @Override
  public List<Apartment> getAll() {
    return apartmentRepo.findAll();
  }

  @Override
  public Apartment findById(Long id) {
    return apartmentRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
  }
}
