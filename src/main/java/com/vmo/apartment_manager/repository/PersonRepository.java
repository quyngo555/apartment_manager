package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Person;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  @Query(value = "select p from Person p "
      + "left join Contract c on c.person.id = p.id "
      + "inner join Apartment a on a.id = c.apartment.id and a.id = ?1")
  List<Person> findAllByApartmentId(Long idApartment);

}
