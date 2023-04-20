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
import java.util.Date;
import java.util.List;
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
    Apartment apartment = apartmentRepo.findById(contractRequest.getApartment().getId()).get();
    if(contractRepo.checkContractActiveByApartmentId(apartment.getId()) == 0){
      apartment.setStatus(true);
      apartmentRepo.save(apartment);
      contract.setApartment(apartment);
    }else{
      throw new Exception(ConstantError.CONTRACT_EXISTS);
    }
    contract.setStatus(ContractStatus.ACTIVE);
    Person person = personRepo.findById(contractRequest.getPerson().getId()).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + contractRequest.getPerson().getId());
    });
    contract.setPriceApartment(contractRequest.getPriceApartment());
    contract.setStartDate(contractRequest.getStartDate());
    contract.setEndDate(contractRequest.getEndDate());
    contract.setCode(contractRequest.getCode());
    contract.setPerson(person);
    contract = contractRepo.save(contract);
    if(contractRequest.getLiveHere()){
      person.setContractId(contract.getId());
      person.setStatus(true);
    }
    personRepo.save(person);
    return new ContractResponse(contract);
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
  public Contract changeRepersent(long id, long newPresent) {
    Contract contractOld = contractRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    contractOld.setStatus(ContractStatus.TERMINATE);
    Person representOld = personRepo.findRepresentByContractId(contractOld.getId());
    representOld.setStatus(false);
    contractRepo.save(contractOld);
    Apartment apartment = apartmentRepo.findApartmentByContractId(contractOld.getId()).get();
    List<Person> personList = personRepo.findPersonByContractId(contractOld.getId());
    Contract contractNew = new Contract();
    contractNew.setStatus(ContractStatus.ACTIVE);
    contractNew.setApartment(apartment);
    contractNew.setPerson(personRepo.findById(newPresent).get());
    contractNew.setPriceApartment(contractOld.getPriceApartment());
    contractNew.setEndDate(contractOld.getEndDate());
    contractNew.setStartDate(contractOld.getStartDate());
    contractNew = contractRepo.save(contractNew);
    for(Person person: personList){
      person.setContractId(contractNew.getId());
    }
    personRepo.saveAll(personList);
    return contractNew;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public String changeAllStatusByIds(long[] ids) {
    for(long id: ids){
      Contract contract = contractRepo.findByRepresent(id);
      if(contract != null){
        contract.setStatus(ContractStatus.TERMINATE);
        personRepo.findPersonByContractId(contract.getId()).stream()
            .forEach(person -> person.setStatus(false));
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
    return contractRepo.findContractByApartmentId(apartmentId).get();
  }

  @Scheduled(cron = "0 10 01 ? * ?") // Run at 10h on 01th of month
  public void autoChangeStatus(){
    List<Contract> contracts = contractRepo.findContractActive();
    for(Contract contract: contracts){
      Date endDate = contract.getEndDate();
      Date currentDate = new Date();
      long getDiff = endDate.getTime() - currentDate.getTime() ;
      long getDayDiff = TimeUnit.MILLISECONDS.toDays(getDiff);
      if(getDayDiff <= 30){
        contract.setStatus(ContractStatus.WARNING);
        contractRepo.save(contract);
      }
    }
  }

  @Scheduled(cron = "0 10 * ? * ?") // Run at 10h on 01th of month
  public void checkContractExpired(){
    List<Contract> contracts = contractRepo.findContractActive();
    for(Contract contract: contracts){
      Date endDate = contract.getEndDate();
      Date currentDate = new Date();
      long getDiff = endDate.getTime() - currentDate.getTime() ;
      long getDayDiff = TimeUnit.MILLISECONDS.toDays(getDiff);
      if(getDayDiff == 30){
        contract.setStatus(ContractStatus.WARNING);

      }else if(getDayDiff == 0){
        contract.setStatus(ContractStatus.EXPIRED);
        personRepo.findPersonByContractId(contract.getId()).stream()
            .forEach(person -> person.setStatus(false));
      }
      contractRepo.save(contract);
    }
  }

}
