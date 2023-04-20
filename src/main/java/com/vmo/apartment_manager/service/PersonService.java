package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.payload.request.PersonRequest;
import com.vmo.apartment_manager.payload.response.PersonResponse;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import java.util.List;

public interface PersonService {
  PersonResponse add(PersonRequest person);
  PersonResponse update(Long id, Person person);
  List<PersonResponse> getPersonsActiveByApartmentId(long id);
  String deletePersonById(Long id);
  String deletePersonsById(long[] ids);
  List<PersonResponse> getPersonByName(String namePerson);
  List<PersonResponse> getRepresent(Integer pageNo, Integer pageSize, String sortBy);
  List<PersonResponse> getPersonsByRepresent(long representId);
  List<Person> findAll();
  List<Person> findPersonActive(long apartmentId);
}
