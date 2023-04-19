package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.payload.request.ContractRequest;
import com.vmo.apartment_manager.payload.response.ContractResponse;
import java.util.Date;
import java.util.List;

public interface ContractService {
  ContractResponse add(ContractRequest contract) throws Exception;
  Contract update(Long id, Contract contract);
  Contract findById(Long id);
  List<Contract> getAll(Integer pageNo, Integer pageSize, String sortBy);
  Contract changeRepersent(long id, long newPresent);
  String changeAllStatusByIds(long[] ids);
  List<Contract> findContractByCreatedBetween(Date startDate, Date endDate);
  Contract findContractByApartmentId(long apartmentId);
  void autoChangeStatus();
}
