package com.vmo.apartment_manager;

import com.vmo.apartment_manager.controller.BillController;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class InitData {

  @Autowired
  private ApartmentRepository apartmentrepo;

  @Autowired
  private PersonRepository personRepo;

  @Autowired
  private ContractRepository contractRepo;

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private BillDetailRepository billDetailRepo;

  @Autowired
  private ServiceFeeRepository serviceFeeRepo;

  @Test
  void testAddApartment(){
    Apartment apartment = new Apartment(1l, "CT011111", "CT01", 1000d, false, "good");
    apartmentrepo.save(apartment);
    System.out.println(apartment.getName());
  }

}
