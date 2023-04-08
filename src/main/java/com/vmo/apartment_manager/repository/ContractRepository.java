package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
  @Query(value = "select c from Contract c inner join Person p on p.id = c.person.id and p.id = ?1")
  Contract findByIdPerson(long id);
  @Query(value = "select c from Contract c "
      + "inner join Apartment a on a.id = c.apartment.id and c.status = ?1")
  List<Contract> findAllByApartmentId(Long idApartment);
}
