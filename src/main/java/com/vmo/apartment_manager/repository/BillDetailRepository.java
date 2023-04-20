package com.vmo.apartment_manager.repository;

import com.vmo.apartment_manager.entity.BillDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {


}
