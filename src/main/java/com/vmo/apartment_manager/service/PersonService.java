package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.payload.request.PersonRequest;
import com.vmo.apartment_manager.payload.response.PersonResponse;
import com.vmo.apartment_manager.entity.Person;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface PersonService {
  PersonResponse add(PersonRequest person);
  PersonResponse update(Long id, PersonRequest person);
  List<PersonResponse> getPersonsActiveByApartmentId(long id);
  String deletePersonsById(long[] ids);
  List<PersonResponse> getPersonByName(String namePerson);
  Page<PersonResponse> getRepresent(Integer pageNo, Integer pageSize, String sortBy);
  List<Person> findAll();
  List<PersonResponse> findPersonsByRepresent(String apartmentCode);
  Person findById(long id);
}
