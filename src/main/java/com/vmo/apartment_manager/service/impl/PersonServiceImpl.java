package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.PersonDto;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.PersonService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  @Autowired
  PersonRepository personRepo;

  @Autowired
  ApartmentRepository apartmentRepo;

  @Override
  public PersonDto add(Person person) {
    if(person.getIdParent() == null && (person.getEmail() == null || person.getPhone() == null))
      throw new NotFoundException(ConstantError.LACK_OF_EMAIL_PHONE);
    Person person1 = personRepo.save(person);
    String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person1.getId());
    PersonDto personDto = new PersonDto(person1, apartmentName);
    return personDto;
  }

  @Override
  public PersonDto update(Long id, Person person) {
    Person person1= personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person1.setPhone(person.getPhone());
    person1.setFullName(person.getFullName());
    person1.setIdParent(person.getIdParent());
    person1.setDob(person.getDob());
    person1.setEmail(person.getEmail());
    person1.setCin(person.getCin());
    person1.setGender(person.getGender());
    person1.setCarrer(person.getCarrer());
    person1 = personRepo.save(person1);

    String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person1.getId());
    PersonDto dto = new PersonDto(person1, apartmentName);
    return dto;
  }

  @Override
  public List<PersonDto> getAll() {
    List<Person> personList = personRepo.findAll();
    List<PersonDto> personDtos = new ArrayList<>();
    for(Person person : personList){
      String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
      personDtos.add(new PersonDto(person, apartmentName));
    }
    return personDtos;

  }

  @Override
  public PersonDto findById(Long id) {
    Person person =  personRepo.findById(id).orElseThrow(()->{
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
    return new PersonDto(person, apartmentName);

  }

  @Override
  public List<PersonDto> getAllByApartmentId(Long id) {

    List<Person> personList = personRepo.findAll();
    List<PersonDto> personDtos = new ArrayList<>();
    for(Person person : personList){
      String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
      personDtos.add(new PersonDto(person, apartmentName));
    }
    return personDtos;
  }
}
