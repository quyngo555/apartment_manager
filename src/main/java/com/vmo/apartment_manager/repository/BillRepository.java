package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Contract;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillRepository extends JpaRepository<Bill, Long> {
  List<Bill> findByCreatedDateBetween(Date startDate, Date endDate);
  @Query(value = "select b from Bill b where b.stauts = true")
  List<Bill> findBillUnPaid();
}
