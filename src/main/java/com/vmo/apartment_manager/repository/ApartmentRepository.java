package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Person;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
  @Query(value = "select a from Apartment a where a.name like %?1%")
  List<Apartment> findApartmentByNameWithPagination(String apartmentName, Pageable pageable);

  @Query(value = "select a from Apartment a "
      + "inner join Contract c on a.id = c.apartment.id "
      + "where c.person.id = ?1 and c.status = 0")
  Apartment findByRepresentId(long personId);

  @Query(value = "select count(a) from Apartment a where a.status = true ")
  int countApartmentInUse();

  @Query(value = "select a from Apartment a "
      + "inner join Contract c on a.id = c.apartment.id "
      + "where c.id = ?1 and c.status = 0")
  Optional<Apartment> findApartmentByContractId(long contractId);

  @Query(value = "select a from Apartment a where a.status = false")
  List<Apartment> findApartmentActive();

  @Query(value = "select a from Apartment a where a.status = true")
  List<Apartment> findApartmentUnActive();
}
