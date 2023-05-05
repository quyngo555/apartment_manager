package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

  @Query(value = "select count(c) from Contract c")
  int countContact();

  @Query(value = "select c from Contract c "
      + "where c.apartment.id = ?1 and c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE")
  Optional<Contract> findContractByApartmentId(long apartmentId);

  @Query(value = "select count(c) from Contract c "
      + "inner join Apartment a on c.apartment.id = a.id "
      + "where c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE and a.id = ?1")
  int checkContractActiveByApartmentId(Long idApartment);

  @Query(value = "select c from Contract c where c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE")
  List<Contract> findContractActive();

  @Query(value = "select c from Contract c where c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE")
  Page<Contract> findContractActiveWithPagination(Pageable pageable);

  Optional<Contract> findContractByCode(String code);

  @Query(value = "select c from Contract c "
      + "inner join Apartment a on c.apartment.id = a.id "
      + "where a.code = ?1 and c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE")
  Optional<Contract> findContractByApartmentCode(String apartmentCode);
}
