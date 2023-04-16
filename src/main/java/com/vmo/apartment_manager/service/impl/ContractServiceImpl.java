package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.ContractService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  public Contract add(Contract contract) throws Exception {
    Apartment apartment = apartmentRepo.findById(contract.getApartment().getId()).get();
    if(contractRepo.checkContractActiveByApartmentId(apartment.getId()) == 0){
      apartment.setStatus(1);
      contract.setApartment(apartment);
    }else{
      throw new Exception(ConstantError.CONTRACT_EXISTS);
    }
    Person person = personRepo.findById(contract.getPerson().getId()).get();
    contract.setCode(contract.getCode());
    contract.setPerson(person);
    apartmentRepo.save(apartment);
    return contractRepo.save(contract);
  }

  @Override
  public Contract update(Long id, Contract contract) {
    Contract contract1 = contractRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    contract1.setPerson(personRepo.findById(contract.getPerson().getId()).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + contract.getPerson().getId());
    }));
    contract1.setApartment(apartmentRepo.findById(contract.getApartment().getId()).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + contract.getApartment().getId());
    }));
    contract1.setCode(contract.getCode());
    contract1.setEndDate(contract.getEndDate());
    contract1.setStartDate(contract.getStartDate());
    contract1.setStatus(contract.getStatus());
    contract1.setPriceApartment(contract.getPriceApartment());
    contract1 = contractRepo.save(contract1);
    return contract1;
  }

  @Override
  public Contract findById(Long id) {
    return contractRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
  }

  @Override
  public List<Contract> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    return contractRepo.findAll(paging).getContent();
  }

  @Override
  public String changeStatusById(long id) {
    Contract contract = contractRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    contract.setStatus(0);
    contractRepo.save(contract);
    return "Change status succedd!";
  }

  @Override
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public String changeAllStatusByIds(long[] ids) {
    for(long id: ids){
      Contract contract = contractRepo.findByRepresent(id);
      if(contract != null){
        contract.setStatus(0);
        contractRepo.save(contract);
      }

    }
    return "Change status succedd!";
  }



  @Override
  public List<Contract> findContractByCreatedBetween(Date startDate, Date endDate) {
    return contractRepo.findByCreatedDateBetween(startDate, endDate);
  }

  @Override
  public Contract findContractByApartmentId(long apartmentId) {
    return contractRepo.findContractByApartmentId(apartmentId);
  }
}
