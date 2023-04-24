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
        personRepo.save(person1);
        return new PersonResponse(person1, apartment.getCode());
      }else{
       personRepo.save(person1);
      }
    }else{
      personRepo.save(person1);
    }
    return new PersonResponse(person1);
  }

  @Override
  public PersonResponse update(Long id, Person person) {
    Person person1 = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    if(person1 != null){
      person.setId(id);
    }
    personRepo.save(person);
    if(person.getContractId() != null){
      person1.setStatus(person.getStatus());
      Apartment apartment = null;
      apartment = apartmentRepo.findApartmentByContractId(person1.getContractId()).get();
      return new PersonResponse(person1, apartment.getCode());
    }else{
      return new PersonResponse(person1);
    }
  }

  @Override
  public List<PersonResponse> getPersonsActiveByApartmentId(long id) {
    Apartment apartment = apartmentRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
    Contract contract = contractRepo.findContractByApartmentId(apartment.getId()).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_EXISTS_IN_APARTMENT + apartment.getId());
    });
    List<Person> personList = personRepo.findAllByContractId(contract.getId());

    List<PersonResponse> personResponses = new ArrayList<>();
    for (Person person : personList) {
        personResponses.add(new PersonResponse(person, apartment.getName()));
    }
    return personResponses;
  }



  @Override
  public String deletePersonsById(long[] ids) {
    for (long id : ids) {
      Person person = personRepo.findById(id).orElseThrow(() -> {
        throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
      });
      person.setStatus(false);
      personRepo.save(person);
    }
    return "Delete succedd!";
  }

  @Override
  public List<PersonResponse> getPersonByName(String namePerson) {
    List<Person> personList = personRepo.findPersonByName(namePerson);
    List<PersonResponse> personResponses = new ArrayList<>();
    for (Person person : personList) {
      Apartment apartment = apartmentRepo.findApartmentByContractId(person.getContractId()).orElseThrow(()->{
        throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
      });
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
      List<Contract> contracts = contractRepo.findByRepresent(person.getId());
      for(Contract contract: contracts){
        Apartment apartment = apartmentRepo.findApartmentByContractId(contract.getId()).orElseThrow(() ->{
          throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
        });
        personResponses.add(new PersonResponse(person, apartment.getCode()));
      }
    }
    return personResponses;
  }

  @Override
  public List<Person> findAll() {
    return personRepo.findAll();
  }

  @Override
  public List<Person> findPersonsByApartmentCode(String apartmentCode) {
    Contract contract = contractRepo.findContractByApartmentCode(apartmentCode).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND);
    });
    return personRepo.findPersonByContractId(contract.getId());
  }

  @Override
  public Person findById(long id) {
    return personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
  }


}
