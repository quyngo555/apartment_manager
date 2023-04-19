package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {
  ServiceFee findServiceFeeByName(String serviceFeeName);
}
