package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.request.PersonRequest;
import com.vmo.apartment_manager.payload.response.PersonResponse;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.PersonService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  public PersonResponse add(PersonRequest person) {
    Person person1 = new Person();

    person1.setStatus(false);
    person1.setCin(person.getCin());
    person1.setDob(person.getDob());
    person1.setCarrer(person.getCarrer());
    person1.setGender(person.getGender());
    person1.setFullName(person.getFullName());
    person1.setPhone(person.getPhone());
    person1.setEmail(person.getEmail());

    if(person.getApartmentId() != null){
      Apartment apartment = apartmentRepo.findById(person.getApartmentId()).get();
      Optional<Contract> contract = contractRepo.findContractByApartmentId(apartment.getId());
      if(contract.isEmpty() == false){
        person1.setContractId(contract.get().getId());
        person1.setStatus(true);
        person1 = personRepo.save(person1);
        return new PersonResponse(person1, apartment.getCode());
      }else{
        person1 = personRepo.save(person1);
      }
    }else{
      person1 = personRepo.save(person1);
    }


    return new PersonResponse(person1);
  }

  @Override
  public PersonResponse update(Long id, Person person) {
    Person person1 = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    Apartment apartment = null;
    person1.setPhone(person.getPhone());
    person1.setFullName(person.getFullName());
    person1.setDob(person.getDob());
    person1.setEmail(person.getEmail());
    person1.setCin(person.getCin());
    person1.setGender(person.getGender());
    person1.setCarrer(person.getCarrer());
    person1 = personRepo.save(person1);
    if(person1.getContractId() != null){
      person1.setStatus(person.getStatus());
      apartment = apartmentRepo.findApartmentByContractId(person1.getContractId()).get();
      return new PersonResponse(person1, apartment.getCode());
    }else{
      return new PersonResponse(person1);
    }
  }

  @Override
  public List<PersonResponse> getPersonsActiveByApartmentId(long id) {
    Apartment apartment = apartmentRepo.findById(id).get();
    Contract contract = contractRepo.findContractByApartmentId(apartment.getId()).get();
    List<Person> personList = personRepo.findAllByContractId(contract.getId());

    List<PersonResponse> personResponses = new ArrayList<>();
    for (Person person : personList) {
        personResponses.add(new PersonResponse(person, apartment.getName()));
    }
    return personResponses;
  }

  @Override
  public String deletePersonById(Long id) {
    Person person = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person.setStatus(false);
    personRepo.save(person);
    return "Delete Succedd.";
  }

  @Override
  public String deletePersonsById(long[] ids) {
    for (long id : ids) {
      Person person = personRepo.findById(id).get();
      person.setStatus(false);
      personRepo.save(person);
    }
    return "Delete succedd!";
  }

  @Override
  public List<PersonResponse> getPersonByName(String namePerson) {
    List<Person> personList = personRepo.getPersonByName(namePerson);
    List<PersonResponse> personResponses = new ArrayList<>();
    for (Person person : personList) {
      Apartment apartment = apartmentRepo.findApartmentByContractId(person.getContractId()).get();
        personResponses.add(new PersonResponse(person, apartment.getName()));
    }
    return personResponses;
  }

  @Override
  public List<PersonResponse> getRepresent(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    List<Person> personList = personRepo.findRepresentWithPagination(paging);
    List<PersonResponse> personResponses = new ArrayList<>();
    for(Person person: personList){
      Optional<Apartment> apartment = apartmentRepo.findApartmentByContractId(person.getId());
      if(apartment.isEmpty() == false)
      personResponses.add(new PersonResponse(person, apartment.get().getCode()));
    }
    return personResponses;
  }

  @Override
  public List<PersonResponse> getPersonsByRepresent(long representId) {
    return personRepo.findPersonsByRepresentId(representId).stream()
        .map(PersonResponse::new)
        .toList();

  }

  @Override
  public List<Person> findAll() {
    return personRepo.findAll();
  }


}
