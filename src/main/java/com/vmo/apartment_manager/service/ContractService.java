package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.payload.request.ContractRequest;
import com.vmo.apartment_manager.payload.response.ContractResponse;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;

public interface ContractService {
  ContractResponse add(ContractRequest contract) throws Exception;
  Contract update(Long id, Contract contract);
  ContractResponse findById(Long id);
  Page<ContractResponse> getAllContractActive(Integer pageNo, Integer pageSize, String sortBy);
  Contract changeRepersent(long id, long newPresent);
  String changeAllStatusByIds(long[] ids);
  List<ContractResponse> findContractByCreatedBetween(Date startDate, Date endDate, Integer pageNo, Integer pageSize, String sortBy);
  ContractResponse findContractByApartmentId(long apartmentId);
  void checkContractExpired();
}
