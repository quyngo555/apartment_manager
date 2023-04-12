package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.ApartmentDto;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.ApartmentService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ApartmentServiceImpl implements ApartmentService {

  @Autowired
  ApartmentRepository apartmentRepo;

  @Autowired
  PersonRepository personRepo;

  @Autowired
  ContractRepository contractRepo;

  @Override
  public List<ApartmentDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    List<Apartment> apartments = apartmentRepo.findAll(paging).getContent();
    List<ApartmentDto> apartmentDtos = new ArrayList<>();
    for(Apartment apartment:apartments){
      Person person = personRepo.findRepresentByApartmentId(apartment.getId());
      List<Person> persons = new ArrayList<>();
      ApartmentDto dto = new ApartmentDto();
      if(person != null){
        persons = personRepo.findAllByParentId(person.getId());
        persons.add(person);
        dto.setRoomMaster(person.getFullName());
      }
      dto.setId(apartment.getId());
      dto.setStatus(apartment.getStatus());
      dto.setArea(apartment.getArea());
      dto.setApartmentName(apartment.getName());
      dto.setPersonInApartment(persons.size());
      apartmentDtos.add(dto);
    }
    return apartmentDtos;
  }

  @Override
  public Apartment findById(Long id) {
    return apartmentRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
  }

  @Override
  public List<Apartment> getApartmentsAvailable() {
    List<Apartment> apartments = apartmentRepo.findAll();
    List<Apartment> apartments1 = new ArrayList<>();
    for(Apartment apartment:apartments){
      if(contractRepo.getContractByApartmentId(apartment.getId()) == null)
        apartments1.add(apartment);
    }
    return apartments1;
  }

  @Override
  public List<Apartment> getApartmentsUnAvailable() {
    List<Apartment> apartments = apartmentRepo.findAll();
    List<Apartment> apartments1 = new ArrayList<>();
    for(Apartment apartment:apartments){
      if(contractRepo.getContractByApartmentId(apartment.getId()) != null)
        apartments1.add(apartment);
    }
    return apartments1;
  }

  @Override
  public ApartmentDto findApartmentByName(Apartment apartment) {
    Apartment apartment1= apartmentRepo.findApartmentByName(apartment.getName());
    Person person = personRepo.findRepresentByApartmentId(apartment1.getId());
    List<Person> persons = new ArrayList<>();
    ApartmentDto dto = new ApartmentDto();
    if(person != null){
      persons = personRepo.findAllByParentId(person.getId());
      persons.add(person);
      dto.setRoomMaster(person.getFullName());
    }
    dto.setId(apartment1.getId());
    dto.setStatus(apartment1.getStatus());
    dto.setArea(apartment1.getArea());
    dto.setApartmentName(apartment1.getName());
    dto.setPersonInApartment(persons.size());
    return dto;
  }
  public List<ApartmentDto> findApartmentByRepresent(String representName){
    List<Person> personList = personRepo.getRepresentByName(representName);

    List<Apartment> apartments= new ArrayList<>();
    for(Person p: personList){
      apartments.addAll(apartmentRepo.findAllByRepresentId(p.getId()));
    }
    List<ApartmentDto> apartmentDtos = new ArrayList<>();
    for(Apartment apartment:apartments){
      Person person1 = personRepo.findRepresentByApartmentId(apartment.getId());
      List<Person> persons = new ArrayList<>();
      ApartmentDto dto = new ApartmentDto();
      if(person1 != null){
        persons = personRepo.findAllByParentId(person1.getId());
        persons.add(person1);
        dto.setRoomMaster(person1.getFullName());
      }
      dto.setId(apartment.getId());
      dto.setStatus(apartment.getStatus());
      dto.setArea(apartment.getArea());
      dto.setApartmentName(apartment.getName());
      dto.setPersonInApartment(persons.size());
      apartmentDtos.add(dto);
    }
    return apartmentDtos;
  }
}
