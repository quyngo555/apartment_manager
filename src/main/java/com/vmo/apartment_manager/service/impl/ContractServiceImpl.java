package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.request.ContractRequest;
import com.vmo.apartment_manager.payload.response.ContractResponse;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.ContractService;
import java.sql.Date;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractServiceImpl implements ContractService {

  @Autowired
  ContractRepository contractRepo;

  @Autowired
  ApartmentRepository apartmentRepo;

  @Autowired
  PersonRepository personRepo;

  @Override
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public ContractResponse add(ContractRequest contractRequest) throws Exception {
    Contract contract = new Contract();
    Apartment apartment = apartmentRepo.findById(contractRequest.getApartment().getId())
        .orElseThrow(() -> {
          throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
        });
    if (contractRepo.checkContractActiveByApartmentId(apartment.getId()) == 0) {
      apartment.setStatus(true);
      apartmentRepo.save(apartment);
      contract.setApartment(apartment);
    } else {
      throw new NotFoundException(ConstantError.CONTRACT_EXISTS);
    }
    contract.setStatus(ContractStatus.ACTIVE);
    Person person = personRepo.findById(contractRequest.getPerson().getId()).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND);
    });
    if (person.getPhone() == null || person.getEmail() == null) {
      throw new NotFoundException(ConstantError.LACK_OF_EMAIL_PHONE);
    }

    contract.setPriceApartment(contractRequest.getPriceApartment());
    contract.setStartDate(contractRequest.getStartDate());
    contract.setEndDate(contractRequest.getEndDate());
    contract.setCode(contractRequest.getCode());
    contract.setPerson(person);
    contractRepo.save(contract);
    if (contractRequest.getLiveHere()) {
      person.setContractId(contract.getId());
      person.setStatus(true);
    }
    personRepo.save(person);
    return new ContractResponse(contract);
  }

  @Override
  public Contract update(Long id, Contract contract) {
    Contract contract1 = contractRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    contract1.setPerson(personRepo.findById(contract.getPerson().getId()).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + contract.getPerson().getId());
    }));
    contract1.setApartment(
        apartmentRepo.findById(contract.getApartment().getId()).orElseThrow(() -> {
          throw new NotFoundException(
              ConstantError.APARTMENT_NOT_FOUND + contract.getApartment().getId());
        }));
    contract.setId(id);
    contractRepo.save(contract);
    return contract;
  }

  @Override
  public ContractResponse findById(Long id) {
    Contract contract =  contractRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    ContractResponse response = new ContractResponse(contract);
    return response;
  }

  @Override
  public List<ContractResponse> getAllContractActive(Integer pageNo, Integer pageSize,
      String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    return contractRepo.findContractActiveWithPagination(paging).stream()
        .map(ContractResponse::new)
        .toList();
  }

  @Override
  public Contract changeRepersent(long id, long newPresent) {
    Contract contractOld = contractRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    contractOld.setStatus(ContractStatus.EXPIRED);
    Person representOld = personRepo.findRepresentByContractId(contractOld.getId())
        .orElseThrow(() -> {
          throw new NotFoundException(ConstantError.PERSON_NOT_FOUND);
        });
    representOld.setStatus(false);
    contractRepo.save(contractOld);
    Apartment apartment = apartmentRepo.findApartmentByContractId(contractOld.getId())
        .orElseThrow(() -> {
          throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND);
        });
    List<Person> personList = personRepo.findPersonByContractId(contractOld.getId());
    Contract contractNew = new Contract();
    contractNew.setStatus(ContractStatus.ACTIVE);
    contractNew.setApartment(apartment);
    Person newRepresent = personRepo.findById(newPresent).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND);
    });
    contractNew.setPerson(newRepresent);
    contractNew.setPriceApartment(contractOld.getPriceApartment());
    contractNew.setEndDate(contractOld.getEndDate());
    contractNew.setStartDate(contractOld.getStartDate());
    contractRepo.save(contractNew);
    for (Person person : personList) {
      person.setContractId(contractNew.getId());
    }
    personRepo.saveAll(personList);
    return contractNew;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public String changeAllStatusByIds(long[] ids) {
    for (long id : ids) {
      Contract contract = contractRepo.findById(id).orElseThrow(() -> {
        throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND);
      });
      contract.setStatus(ContractStatus.TERMINATE);
      contract.getApartment().setStatus(false);
      apartmentRepo.save(contract.getApartment());
      personRepo.findPersonByContractId(contract.getId()).stream()
          .forEach(person -> person.setStatus(false));
    }
    return "Change status succedd!";
  }


  @Override
  public List<ContractResponse> findContractByCreatedBetween(Date startDate, Date endDate,
      Integer pageNo, Integer pageSize, String sortBy) {
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    return contractRepo.findByCreatedDateBetweenDatesWithPagination(startDate, endDate, pageable)
        .stream()
        .map(ContractResponse::new)
        .toList();
  }

  @Override
  public ContractResponse findContractByApartmentId(long apartmentId) {
    Contract contract = contractRepo.findContractByApartmentId(apartmentId).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND);
    });
    return new ContractResponse(contract);
  }

  @Scheduled(cron = "0 10 * ? * ?") // Run at 10h on 01th of month
  public void checkContractExpired() {
    List<Contract> contracts = contractRepo.findContractActive();
    for (Contract contract : contracts) {
      java.util.Date endDate = contract.getEndDate();
      java.util.Date currentDate = new java.util.Date();
      long getDiff = endDate.getTime() - currentDate.getTime();
      long getDayDiff = TimeUnit.MILLISECONDS.toDays(getDiff);
      if (getDayDiff == 30) {
        contract.setStatus(ContractStatus.WARNING);

      } else if (getDayDiff == 0) {
        contract.setStatus(ContractStatus.EXPIRED);
        personRepo.findPersonByContractId(contract.getId()).stream()
            .forEach(person -> person.setStatus(false));
      }
      contractRepo.save(contract);
    }
  }

}
