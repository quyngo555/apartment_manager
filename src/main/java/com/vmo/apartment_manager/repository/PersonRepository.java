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
      + "inner join Contract c on c.person.id = p.id "
      + "inner join Apartment a on a.id = c.apartment.id "
      + "where a.id = ?1")
  Person findRepresentByApartmentId(Long idApartment);

  List<Person> findAllByParentId(long idParent);

  @Query(value = "select p.email from Person p "
      + "inner join Contract c on p.id = c.person.id and c.status = 1 "
      + "inner join Bill b on b.contract.id = c.id and b.id = ?1 "
      + "where p.status = 1")
  String getEmailPresentedByBillId(long billId);

  @Query(value = "select p.id from Person p "
      + "inner join Contract c on c.person.id = p.id "
      + "inner join Apartment a on a.id = c.apartment.id where a.id = ?1")
  long getrepresentIdByApartmentId(long apartmentId);

  @Query(value = "select p from Person p "
      + "where p.fullName like %?1%")
  List<Person> getPersonByName(String namePerson);

  @Query(value = "select p from Person p "
      + "inner join Contract c on p.id = c.person.id and p.parentId is null "
      + "where p.status = 1 and c.status = 1 and p.fullName like %?1%")
  List<Person> getRepresentByName(String representName);
}
