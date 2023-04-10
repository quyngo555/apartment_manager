package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.ContractService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl implements ContractService {

  @Autowired
  ContractRepository contractRepo;

  @Autowired
  ApartmentRepository apartmentRepo;

  @Autowired
  PersonRepository personRepo;

  @Override
  public Contract add(Contract contract) {
    Person person = personRepo.findById(contract.getPerson().getId()).get();
    if (person != null) {
      person.setIdParent(null);
    }
    contract.setPerson(person);
    contract.setApartment(apartmentRepo.findById(contract.getApartment().getId()).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + contract.getApartment().getId());
    }));
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
  public String changeAllStatusByIds(long[] ids) {
    for(long id: ids){
      Contract contract = contractRepo.findByIdPerson(id);
      contract.setStatus(0);
      contractRepo.save(contract);
    }
    return "Change status succedd!";
  }

  @Override
  public List<Contract> findAllByApartmentId(long id) {
    return contractRepo.findAllByApartmentId(id);
  }
}
