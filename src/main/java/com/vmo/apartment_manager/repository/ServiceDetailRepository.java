package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.ServiceDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDetailRepository extends JpaRepository<ServiceDetail, Long> {
  List<ServiceDetail> findAllByBillId(Long id);

}
