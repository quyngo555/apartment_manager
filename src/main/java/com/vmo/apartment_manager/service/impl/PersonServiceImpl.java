package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.DataIntegrityException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
    try{
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
        Apartment apartment = apartmentRepo.findById(person.getApartmentId()).orElseThrow(()->{
          throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
        });
        Optional<Contract> contract = contractRepo.findContractByApartmentId(apartment.getId());
        if(contract.isPresent()){
          person1.setContractId(contract.get().getId());
          person1.setStatus(true);
          personRepo.save(person1);
          return new PersonResponse(person1, apartment);
        }else{
          personRepo.save(person1);
        }
      }else{
        personRepo.save(person1);
      }
      return new PersonResponse(person1);
    }catch (Exception e){
      throw new DataIntegrityException(e.getMessage());
    }

  }

  @Override
  public PersonResponse update(Long id, PersonRequest person) {
    Person person1 = personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person1.setCarrer(person.getCarrer());
    person1.setCin(person.getCin());
    person1.setEmail(person.getEmail());
    person1.setDob(person.getDob());
    Contract contract = contractRepo.findContractByApartmentId(person.getApartmentId()).orElseThrow(()->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND);
    });
    person1.setContractId(contract.getId());
    person1.setFullName(person.getFullName());
    person1.setStatus(person.getStatus());
    personRepo.save(person1);
    if(person1.getContractId() != null){
      person1.setStatus(person.getStatus());
      Apartment apartment = new Apartment();
      apartment = apartmentRepo.findApartmentByContractId(person1.getContractId()).orElseThrow(()-> {
        throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
      });
      return new PersonResponse(person1, apartment);
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
        personResponses.add(new PersonResponse(person, apartment));
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
      personResponses.add(new PersonResponse(person, apartment));
    }
    return personResponses;
  }

  @Override
  public Page<PersonResponse> getRepresent(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    return contractRepo.findContractActiveWithPagination(paging)
            .map(contract -> new PersonResponse(contract.getPerson(), contract.getApartment()));
  }

  @Override
  public List<Person> findAll() {
    return personRepo.findAll();
  }

  @Override
  public List<PersonResponse> findPersonsByRepresent(String apartmentCode) {
    Contract contract = contractRepo.findContractByApartmentCode(apartmentCode).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND);
    });
    return personRepo.findPersonByContractId(contract.getId()).stream()
            .map(PersonResponse::new)
            .toList();
  }

  @Override
  public PersonResponse findById(long id) {
    Person person =  personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    Apartment apartment = new Apartment();
    if(person.getContractId() != null){
      apartment = apartmentRepo.findApartmentByContractId(person.getContractId()).orElseThrow(()-> {
        throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
      });
      return new PersonResponse(person, apartment);
    }
    return new PersonResponse(person);
  }


}
