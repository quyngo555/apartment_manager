package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.PersonDto;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.PersonService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  @Autowired
  PersonRepository personRepo;

  @Autowired
  ApartmentRepository apartmentRepo;

  @Autowired
  ContractRepository contractRepo;


  @Override
  public PersonDto add(Person person) {
    Person person1 = new Person();
    Contract contract = contractRepo.findContractByApartmentId(person.getApartmentId());
    if (contract == null) {
      person1.setParentId(null);
    } else {
      person1.setParentId(personRepo.getrepresentIdByApartmentId(person.getApartmentId()));
    }
    if (person.getParentId() == null && (person.getEmail() == null || person.getPhone() == null)) {
      throw new NotFoundException(ConstantError.LACK_OF_EMAIL_PHONE);
    }
    person1.setStatus(1);
    person1.setCin(person.getCin());
    person1.setDob(person.getDob());
    person1.setCarrer(person.getCarrer());
    person1.setGender(person.getGender());
    person1.setFullName(person.getFullName());
    person1.setPhone(person.getPhone());
    person1.setEmail(person.getEmail());
    person1.setApartmentId(person.getApartmentId());
    Apartment apartment = apartmentRepo.findById(person.getApartmentId()).get();
    person1 = personRepo.save(person1);
    return new PersonDto(person1, apartment.getCode());
  }

  @Override
  public PersonDto update(Long id, Person person) {
    Person person1 = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person1.setPhone(person.getPhone());
    person1.setFullName(person.getFullName());
    person1.setDob(person.getDob());
    person1.setEmail(person.getEmail());
    person1.setCin(person.getCin());
    person1.setGender(person.getGender());
    person1.setCarrer(person.getCarrer());
    person1 = personRepo.save(person1);
    Apartment apartment = apartmentRepo.findById(person.getApartmentId()).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
    PersonDto dto = new PersonDto(person1, apartment.getCode());
    return dto;
  }

  @Override
  public List<PersonDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    List<Person> personList = personRepo.findAll(paging).getContent();
    List<PersonDto> personDtos = new ArrayList<>();
    for (Person person : personList) {
      Apartment apartment = apartmentRepo.findById(person.getApartmentId()).get();
      personDtos.add(new PersonDto(person, apartment.getCode()));
    }
    return personDtos;
  }

  @Override
  public PersonDto findById(Long id) {
    Person person = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    Apartment apartment = apartmentRepo.findById(person.getId()).get();
    return new PersonDto(person, apartment.getCode());
  }

  @Override
  public List<PersonDto> getAllByApartmentId(Long id, Integer pageNo, Integer pageSize,
      String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    Apartment apartment = apartmentRepo.findById(id).get();
    List<Person> personList = personRepo.findPersonByApartmentId(id);
    List<PersonDto> personDtos = new ArrayList<>();
    for (Person person : personList) {
      personDtos.add(new PersonDto(person, apartment.getCode()));
    }
    return personDtos;
  }

  @Override
  public List<PersonDto> getPersonsActiveByApartmentId(long id) {
    List<Person> personList = personRepo.findPersonByApartmentId(id);
    Apartment apartment = apartmentRepo.findById(id).get();
    List<PersonDto> personDtos = new ArrayList<>();
    for (Person person : personList) {
      if (person.getStatus() == 1) {
        personDtos.add(new PersonDto(person, apartment.getName()));
      }
    }
    return personDtos;
  }

  @Override
  public List<PersonDto> getPersonsUnActiveByApartmentId(long id, Integer pageNo, Integer pageSize,
      String sortBy) {
//    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    Apartment apartment = apartmentRepo.findById(id).get();
    List<Person> personList = personRepo.findPersonByApartmentId(id);
    List<PersonDto> personDtos = new ArrayList<>();
    for (Person person : personList) {
      if (person.getStatus() == 0) {
        personDtos.add(new PersonDto(person, apartment.getName()));
      }
    }
    return personDtos;
  }

  @Override
  public String deletePersonById(Long id) {
    Person person = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person.setStatus(0);
    personRepo.save(person);
    return "Delete Succedd.";
  }

  @Override
  public String deletePersonsById(long[] ids) {
    for (long id : ids) {
      Person person = personRepo.findById(id).get();
      person.setStatus(0);
      personRepo.save(person);
    }
    return "Delete succedd!";
  }

  @Override
  public List<PersonDto> getPersonByName(String namePerson) {
    List<Person> personList = personRepo.getPersonByName(namePerson);
    List<PersonDto> personDtos = new ArrayList<>();
    for (Person person : personList) {
        Apartment apartment = apartmentRepo.findById(person.getApartmentId()).get();
        personDtos.add(new PersonDto(person, apartment.getName()));
    }
    return personDtos;
  }

  @Override
  public List<PersonDto> getRepresent() {
    List<Person> personList = personRepo.findRepresent();
    List<PersonDto> personDtos = new ArrayList<>();
    for(Person person: personList){
      Apartment apartment = apartmentRepo.findById(person.getApartmentId()).get();
      personDtos.add(new PersonDto(person, apartment.getCode()));
    }
    return personDtos;
  }

  @Override
  public List<PersonDto> findPersonByCreatedBetween(Date startDate, Date endDate) {
    List<Person> personList=  personRepo.findByCreatedDateBetween(startDate, endDate);
    List<PersonDto> personDtos = new ArrayList<>();
    for (Person person : personList) {
      Apartment apartment = apartmentRepo.findById(person.getApartmentId()).get();
      personDtos.add(new PersonDto(person, apartment.getCode()));
    }
    return personDtos;
  }


}
