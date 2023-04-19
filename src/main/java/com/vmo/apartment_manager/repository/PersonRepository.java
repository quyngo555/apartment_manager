package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {


  @Query("select p from Person p "
      + "inner join Contract c on c.person.id = p.id "
      + "where c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE")
  Person findRepresentByContractId(long contractId);

  @Query(value = "select p from Person p where p.contractId = ?1 and p.status = true")
  List<Person> findAllByContractId(long contractId);

  @Query(value = "select p from Person p "
      + "where p.fullName like %?1% and p.status = true")
  List<Person> getPersonByName(String namePerson);

  @Query(value = "select p from Person p "
      + "inner join Contract c on p.id = c.person.id "
      + "where c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE and p.fullName like %?1%")
  List<Person> findRepresentByName(String representName);

  @Query(value = "select p from Person p "
      + "inner join Contract c on c.person.id = p.id where c.status = com.vmo.apartment_manager.entity.ContractStatus.ACTIVE")
  List<Person> findRepresentWithPagination(Pageable pageable);


  @Query(value = "select count(p) from Person p where p.contractId = ?1 and p.status = true")
  int countPersonByContractId(long contractId);

  @Query(value = "select p from Person p where p.contractId = ?1")
  List<Person> findPersonsByRepresentId(long representId);

}
