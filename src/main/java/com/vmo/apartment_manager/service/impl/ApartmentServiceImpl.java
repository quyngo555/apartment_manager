package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.ApartmentDto;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.ApartmentService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vmo.apartment_manager.entity.Person;

@Service
public class ApartmentServiceImpl implements ApartmentService {

  @Autowired
  ApartmentRepository apartmentRepo;

  @Autowired
  PersonRepository personRepo;

  @Override
  public Apartment add(Apartment apartment) {
    return apartmentRepo.save(apartment);
  }

  @Override
  public Apartment update(Long id, Apartment apartment) {
    Apartment apartment1 = apartmentRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
    apartment1.setStatus(apartment.getStatus());
    apartment1.setName(apartment.getName());
    apartment1.setCode(apartment.getCode());
    apartment1.setArea(apartment.getArea());
    apartment1.setDescription(apartment.getDescription());
    apartment1 = apartmentRepo.save(apartment1);
    return  apartment1;
  }

  @Override
  public List<ApartmentDto> getAll() {
    List<Apartment> apartments = apartmentRepo.findAll();
    List<ApartmentDto> apartmentDtos = new ArrayList<>();
    for(Apartment apartment:apartments){
      List<Person> persons = personRepo.findAllByApartmentId(apartment.getId());

      ApartmentDto dto = new ApartmentDto();
      for(Person person: persons){
        if(person.getIdParent()!= null){
          dto.setOwnerApartmentName(personRepo.findById(person.getIdParent()).get().getFullName());
          break;
        }else{
          dto.setOwnerApartmentName(personRepo.findById(person.getId()).get().getFullName());
          break;
        }
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
}
