package com.vmo.apartment_manager;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.controller.BillController;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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

  }

  @Test
  void testAddPerson(){

  }

  @Test
  void testAddContract(){
    Person person = personRepo.findById(1l).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND);
    });
    Apartment apartment = new Apartment();


  }

  void initData(){
    Apartment apartment1 = new Apartment(1l, "CT011111", "CT01", 1000d, false, "good");
    Apartment apartment2 = new Apartment(2l, "CT011111", "CT01", 1000d, false, "good");
    Apartment apartment3 = new Apartment(3l, "CT011111", "CT01", 1000d, false, "good");
    apartmentrepo.saveAll(List.of(apartment1, apartment2, apartment3));

    Person person1 = new Person("Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    Person person2 = new Person("Nguyễn văn b", null, "09456788", "abc@gmail.com", "123456788", false, "it", null, false);
    Person person3 = new Person("Nguyễn văn c", null, "09456787", "abc@gmail.com", "123456787", true, "it", null, false);
    personRepo.saveAll(List.of(person1, person2, person3));

    Contract contract1 = new Contract( 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment1, person1);
    Contract contract2 = new Contract( 8888d, Date.valueOf("2023-04-06"), Date.valueOf("2024-08-30"), "A02", ContractStatus.ACTIVE, apartment2, person2);
    contractRepo.saveAll(List.of(contract1, contract2));

    Bill bill1 = new Bill(0d, false, null, "not pay", null, contract1, new ArrayList<>());
    Bill bill2 = new Bill(0d, false, null, "not pay", null, contract2, new ArrayList<>());
    billRepository.saveAll(List.of(bill1, bill2));

    BillDetail billDetail1 = new BillDetail();

  }

}
