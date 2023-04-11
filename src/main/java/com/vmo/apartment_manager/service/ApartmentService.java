package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.ApartmentDto;
import com.vmo.apartment_manager.entity.Apartment;
import java.util.List;

public interface ApartmentService {
  Apartment add(Apartment apartment);
  Apartment update(Long id, Apartment apartment);
  List<ApartmentDto> getAll(Integer pageNo, Integer pageSize, String sortBy);
  Apartment findById(Long id);
  List<Apartment> getApartmentsAvailable();
  List<Apartment> getApartmentsUnAvailable();
  ApartmentDto findApartmentByName(Apartment apartment);

}
