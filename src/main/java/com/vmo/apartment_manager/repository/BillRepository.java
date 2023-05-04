package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Bill;
import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillRepository extends JpaRepository<Bill, Long> {
  List<Bill> findByCreatedDateBetween(Date startDate, Date endDate);
  @Query(value = "select b from Bill b where b.stauts = false")
  List<Bill> findBillUnPaid();

  Page<Bill> findAll(Pageable pageable);
  @Query(value = "select b from Bill b " +
          "inner join Contract c on  c.id = b.contract.id " +
          "inner join Apartment a on a.id = c.apartment.id " +
          "where b.createdDate between ?1 and ?2 and a.id = ?3")
  Page<Bill> findByCreatedDateBetweenDatesWithPagination(Date startDate, Date endDate, String apartmentId, Pageable pageable);

  @Query(value = "select b from Bill b " +
          "where b.createdDate between ?1 and ?2 ")
  Page<Bill> findByCreatedDateBetweenDatesWithPagination(Date startDate, Date endDate, Pageable pageable);
}
