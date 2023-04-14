package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

}
