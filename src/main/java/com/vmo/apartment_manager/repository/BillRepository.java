package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Contract;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
  List<Bill> findByCreatedDateBetween(Date startDate, Date endDate);
}
