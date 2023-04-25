package com.vmo.apartment_manager;

import static org.mockito.Mockito.when;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.impl.BillServiceImpl;
import com.vmo.apartment_manager.service.impl.EmailServiceImpl;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
  @Mock
  JavaMailSender javaMailSender;

  @Mock
  BillRepository billRepository;

  @Mock
  PersonRepository personRepository;

  @InjectMocks
  EmailServiceImpl emailService;

  private Bill bill;
  private BillDetail billDetail;
  private Person person;

  @BeforeEach
  void setup(){

    ServiceFee serviceFee = new ServiceFee(1l, Double.valueOf(2000), "vnd/m3", TypeService.WATER);
    Apartment apartment = new Apartment(1l, "CT01", "CT01", 1000d, false, "good");
    person = new Person(1l, "Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    billDetail = new BillDetail(1l, 0d, 50d, bill, serviceFee);
    Contract contract = new Contract(1l, 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-12-06"), "CH01", ContractStatus.ACTIVE, apartment, person);
    bill = new Bill(1l, 100d, false, Date.valueOf("2023-04-05"), "not paid", null, contract, List.of(billDetail));
  }

  @Test
  void testSendMail(){
    when(billRepository.findById(1l)).thenReturn(Optional.of(bill));
    when(personRepository.findRepresentByContractId(bill.getContract().getId())).thenReturn(Optional.of(person));

    String message = emailService.sendBill(bill);
    Assertions.assertEquals(message, "Send successfully");
  }

  @Test
  void testSendMailError(){
    Long billId = 1l;
    when(billRepository.findById(billId)).thenReturn(Optional.empty());
    String message = emailService.sendBill(bill);
    Assertions.assertEquals(message, "Send failed");
  }
}
