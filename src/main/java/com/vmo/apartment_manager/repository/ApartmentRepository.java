package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
  @Query(value = "select a.name from Apartment a "
      + "inner join Contract c on c.apartment.id = a.id and c.status = 1 "
      + "inner join Person p on ?1 = c.person.id ")
  String getApartmentNameByIdPerson(Long id);
}
