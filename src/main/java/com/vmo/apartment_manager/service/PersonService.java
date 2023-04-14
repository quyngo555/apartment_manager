package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.dto.PersonDto;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import java.util.List;

public interface PersonService {
  PersonDto add(Person person);
  PersonDto update(Long id, Person person);
  List<PersonDto> getAll(Integer pageNo, Integer pageSize, String sortBy);
  PersonDto findById(Long id);
  List<PersonDto> getAllByApartmentId(Long id,Integer pageNo, Integer pageSize, String sortBy);
  List<PersonDto> getPersonsActiveByApartmentId(long id);
  List<PersonDto> getPersonsUnActiveByApartmentId(long id, Integer pageNo, Integer pageSize, String sortBy);
  String deletePersonById(Long id);
  String deletePersonsById(long[] ids);
  List<PersonDto> getPersonByName(String namePerson);
  List<PersonDto> getRepresent();
  List<PersonDto> findPersonByCreatedBetween(Date startDate, Date endDate);
}
