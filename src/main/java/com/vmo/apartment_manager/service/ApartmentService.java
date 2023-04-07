package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Apartment;
import java.util.List;

public interface ApartmentService {
  Apartment add(Apartment apartment);
  Apartment update(Long id, Apartment apartment);
  List<Apartment> getAll();
  Apartment findById(Long id);

}
