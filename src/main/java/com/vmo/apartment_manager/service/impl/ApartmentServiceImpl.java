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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  public List<ApartmentDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    List<Apartment> apartments = apartmentRepo.findAll(paging).getContent();
    List<ApartmentDto> apartmentDtos = new ArrayList<>();
    for(Apartment apartment:apartments){
      Person person = personRepo.findRepresentByApartmentId(apartment.getId());
      List<Person> persons = personRepo.findAllByIdParent(person.getId());
      persons.add(person);
      ApartmentDto dto = new ApartmentDto();

      dto.setRoomMaster(person.getFullName());
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
    return null;
  }

  @Override
  public List<Apartment> getApartmentsUnAvailable() {
    return null;
  }
}
