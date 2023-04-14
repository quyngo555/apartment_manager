package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.ApartmentDto;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Person;
import java.util.List;

public interface ApartmentService {
  List<ApartmentDto> getAll(Integer pageNo, Integer pageSize, String sortBy);
  Apartment findById(Long id);
  List<Apartment> getApartmentsAvailable();
  List<Apartment> getApartmentsUnAvailable();
  List<ApartmentDto> findApartmentByName(Apartment apartment);
  List<ApartmentDto> findApartmentByRepresent(String representName);

}
