package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.PersonDto;
import com.vmo.apartment_manager.entity.Person;
import java.util.Iterator;
import java.util.List;

public interface PersonService {
  PersonDto add(Person person);
  PersonDto update(Long id, Person person);
  List<PersonDto> getAll();
  PersonDto findById(Long id);
  List<PersonDto> getAllByApartmentId(Long id);
  String deletePersonById(Long id);
  String deletePersonsById(long[] ids);
}
