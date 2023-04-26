package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.payload.response.ApartmentResponse;
import com.vmo.apartment_manager.entity.Apartment;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ApartmentService {
  List<ApartmentResponse> getAll(Integer pageNo, Integer pageSize, String sortBy);
  ApartmentResponse findById(Long id);
  List<Apartment> getApartmentsAvailable();
  List<Apartment> getApartmentsUnAvailable();
  List<ApartmentResponse> findApartmentByName(String apartmentName, Integer pageNo, Integer pageSize, String sortBy);
  List<ApartmentResponse> findApartmentByRepresent(String representName);
  int countApartmentInUse();


}
