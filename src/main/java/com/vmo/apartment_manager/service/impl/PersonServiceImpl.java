package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.PersonDto;
import com.vmo.apartment_manager.dto.PersonRequest;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.PersonService;
import java.util.ArrayList;
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
  public PersonDto add(PersonRequest person) {
    Person person1 = new Person();
    Contract contract = contractRepo.getContractByApartmentId(person.getApartmentId());
    if(contract == null){
      person1.setIdParent(null);
    }else{
      person1.setIdParent(personRepo.getrepresentIdByApartmentId(person.getApartmentId()));
    }
    if(person.getIdParent() == null && (person.getEmail() == null || person.getPhone() == null))
      throw new NotFoundException(ConstantError.LACK_OF_EMAIL_PHONE);
    person1.setStatus(1);
    person1.setCin(person.getCin());
    person1.setDob(person.getDob());
    person1.setCarrer(person.getCarrer());
    person1.setGender(person.getGender());
    person1.setFullName(person.getFullName());
    person1.setPhone(person.getPhone());
    person1.setEmail(person.getEmail());
    String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person1.getId());
    person1 = personRepo.save(person1);
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
  public List<PersonDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    List<Person> personList = personRepo.findAll(paging).getContent();
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
  public List<PersonDto> getAllByApartmentId(Long id, Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    Person represent = personRepo.findRepresentByApartmentId(id);
    List<Person> personList = personRepo.findAllByIdParent(represent.getId());
    personList.add(represent);
    List<PersonDto> personDtos = new ArrayList<>();
    for(Person person : personList){
      String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
      personDtos.add(new PersonDto(person, apartmentName));
    }
    return personDtos;
  }

  @Override
  public List<PersonDto> getPersonsActiveByApartmentId(long id, Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    List<Person> personList = personRepo.findAll(paging).getContent();
    List<PersonDto> personDtos = new ArrayList<>();
    for(Person person : personList){
      String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
      if(person.getStatus() == 1)
        personDtos.add(new PersonDto(person, apartmentName));
    }
    return personDtos;
  }

  @Override
  public List<PersonDto> getPersonsUnActiveByApartmentId(long id, Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    List<Person> personList = personRepo.findAll(paging).getContent();
    List<PersonDto> personDtos = new ArrayList<>();
    for(Person person : personList){
      String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
      if(person.getStatus() == 0)
        personDtos.add(new PersonDto(person, apartmentName));
    }
    return personDtos;
  }

  @Override
  public String deletePersonById(Long id) {
    Person person = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person.setStatus(0);
    Contract contract = contractRepo.findByIdPerson(person.getId());
    contract.setStatus(0);
    contractRepo.save(contract);
    personRepo.save(person);
    return "Delete Succedd.";
  }

  @Override
  public String deletePersonsById(long[] ids) {
    for(long id: ids){
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
    for(Person person : personList){
      String apartmentName = apartmentRepo.getApartmentNameByIdPerson(person.getId());
      personDtos.add(new PersonDto(person, apartmentName));
    }
    return personDtos;
  }
}
