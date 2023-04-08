package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Contract;
import java.util.List;

public interface ContractService {
  Contract add(Contract contract);
  Contract update(Long id, Contract contract);
  Contract findById(Long id);
  List<Contract> getAll(Integer pageNo, Integer pageSize, String sortBy);
  String changeStatusById(long id);
  String changeAllStatusByIds(long[] ids);
  List<Contract> findAllByApartmentId(long id);
}
