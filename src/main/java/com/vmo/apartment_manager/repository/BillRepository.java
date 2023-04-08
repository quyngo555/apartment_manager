package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
  @Query(value = "select b from Bill b "
      + "inner join Contract c on b.contract.id = c.id and c.status = 1 "
      + "inner join Apartment a on a.id = c.apartment.id and a.id = ?1")
  List<Bill> getBillsByApartmentId(long billId);
}
