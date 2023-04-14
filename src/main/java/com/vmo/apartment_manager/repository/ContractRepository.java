package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
  @Query(value = "select c from Contract c "
      + "inner join Person p on p.id = c.person.id "
      + "where c.id = ?1")
  Contract findByRepresent(long id);
  @Query(value = "select c from Contract c "
      + "inner join Apartment a on a.id = c.apartment.id "
      + "and c.status = ?1")
  List<Contract> findAllByApartmentId(Long idApartment);

  @Query(value = "select c from Contract c "
      + "inner join Apartment a on c.apartment.id = ?1 "
      + "where c.status = 1")
  Contract findContractByApartmentId(Long apartmentId);

  @Query(value = "select count(c) from Contract c "
      + "inner join Apartment a on c.apartment.id = a.id "
      + "where c.status = 1 and a.id = ?1")
  int checkContractActiveByApartmentId(Long idApartment);

  @Query(value = "select c from Contract c where c.status > 0")
  List<Contract> findContractActive();

  List<Contract> findByCreatedDateBetween(Date startDate, Date endDate);
}
