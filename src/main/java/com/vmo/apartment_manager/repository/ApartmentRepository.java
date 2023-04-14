package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Person;
import java.util.List;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
  @Query(value = "select a from Apartment a where a.name like %?1%")
  List<Apartment> getApartmentByName(String apartmentName);

  @Query(value = "select a from Apartment a "
      + "inner join Contract c on a.id = c.apartment.id and c.status = 1 "
      + "inner join Person p on ?1 = c.person.id and p.status = 1")
  List<Apartment> findAllByRepresentId(long personId);




}
