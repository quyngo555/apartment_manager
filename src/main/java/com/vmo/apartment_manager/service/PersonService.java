package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Person;
import java.util.List;

public interface PersonService {
  Person add(Person person);
  Person update(Long id, Person person);
  List<Person> getAll();
  Person findById(Long id);
  List<Person> getAllByApartmentId(Long id);
}
