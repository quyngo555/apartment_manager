package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.PersonDto;
import com.vmo.apartment_manager.dto.PersonRequest;
import com.vmo.apartment_manager.entity.Person;
import java.util.Iterator;
import java.util.List;

public interface PersonService {
  PersonDto add(PersonRequest person);
  PersonDto update(Long id, Person person);
  List<PersonDto> getAll(Integer pageNo, Integer pageSize, String sortBy);
  PersonDto findById(Long id);
  List<PersonDto> getAllByApartmentId(Long id,Integer pageNo, Integer pageSize, String sortBy);
  List<PersonDto> getPersonsActiveByApartmentId(long id, Integer pageNo, Integer pageSize, String sortBy);
  List<PersonDto> getPersonsUnActiveByApartmentId(long id, Integer pageNo, Integer pageSize, String sortBy);
  String deletePersonById(Long id);
  String deletePersonsById(long[] ids);
  List<PersonDto> getPersonByName(String namePerson);
}
