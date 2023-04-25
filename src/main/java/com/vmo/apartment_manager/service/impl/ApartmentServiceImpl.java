package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.payload.response.ApartmentResponse;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.ApartmentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  public List<ApartmentResponse> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    List<Apartment> apartments = apartmentRepo.findAll(paging).getContent();

    List<ApartmentResponse> apartmentResponses = new ArrayList<>();
    for (Apartment apartment : apartments) {
      ApartmentResponse dto = new ApartmentResponse();
      Optional<Contract> contract = contractRepo.findContractByApartmentId(apartment.getId());
      Person person = null;
      dto.setArea(apartment.getArea());
      if (contract.isEmpty() == false) {
        dto.setContractCode(contract.get().getCode());

        person = contract.get().getPerson();
        dto.setPersonInApartment(personRepo.countPersonByContractId(contract.get().getId()));
      }
      if(person != null)
        dto.setRoomMaster(person.getFullName());
      dto.setId(apartment.getId());
      dto.setStatus(apartment.getStatus());
      dto.setApartmentCode(apartment.getCode());

      apartmentResponses.add(dto);
    }
    return apartmentResponses;
  }

  @Override
  public Apartment findById(Long id) {
    return apartmentRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + id);
    });
  }

  @Override
  public List<Apartment> getApartmentsAvailable() {
    return apartmentRepo.findApartmentActive();
  }

  @Override
  public List<Apartment> getApartmentsUnAvailable() {
    return apartmentRepo.findApartmentUnActive();
  }

  @Override
  public List<ApartmentResponse> findApartmentByName(String apartmentName, Integer pageNo,Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    List<Apartment> apartments = apartmentRepo.findApartmentByNameWithPagination(apartmentName, paging);
    List<ApartmentResponse> apartmentResponses = new ArrayList<>();
    for (Apartment apartment : apartments) {
      ApartmentResponse dto = new ApartmentResponse();
      Optional<Contract> contract = contractRepo.findContractByApartmentId(apartment.getId());

      if(contract.isEmpty() == false){
        dto.setRoomMaster(contract.get().getPerson().getFullName());
        dto.setPersonInApartment(personRepo.countPersonByContractId(contract.get().getId()));
      }
      dto.setStatus(apartment.getStatus());
      dto.setId(apartment.getId());

      dto.setContractCode(apartment.getCode());
      dto.setApartmentCode(apartment.getCode());
      apartmentResponses.add(dto);
    }
    return apartmentResponses;
  }

  public List<ApartmentResponse> findApartmentByRepresent(String representName) {
    List<Person> representList = personRepo.findRepresentByName(representName);

    List<Apartment> apartments = new ArrayList<>();
    for (Person p : representList) {
      apartments.addAll(apartmentRepo.findByRepresentId(p.getId()));
    }
    List<ApartmentResponse> apartmentResponses = new ArrayList<>();
    for (Apartment apartment : apartments) {
      Optional<Contract> contract = contractRepo.findContractByApartmentId(apartment.getId());

      ApartmentResponse dto = new ApartmentResponse();
      if (contract.isEmpty() == false) {

        dto.setContractCode(contract.get().getCode());
        dto.setRoomMaster(contract.get().getPerson().getFullName());
        dto.setPersonInApartment(personRepo.countPersonByContractId(contract.get().getId()));
      }
      dto.setStatus(apartment.getStatus());
      dto.setId(apartment.getId());

      dto.setApartmentCode(apartment.getCode());

      apartmentResponses.add(dto);
    }
    return apartmentResponses;
  }

  @Override
  public int countApartmentInUse() {
    return apartmentRepo.countApartmentInUse();
  }
}
