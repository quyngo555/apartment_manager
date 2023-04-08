package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
  @Query(value = "select c from Contract c inner join Person p on p.id = c.person.id and p.id = ?1")
  Contract findByIdPerson(long id);
}
