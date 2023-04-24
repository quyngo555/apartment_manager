package com.vmo.apartment_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import com.vmo.apartment_manager.payload.request.BillRequest;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.service.impl.BillServiceImpl;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {

  @Mock
  BillRepository billRepo;

  @Mock
  ContractRepository contractRepo;

  @InjectMocks
  BillServiceImpl billService;

  private Bill bill = new Bill();
  private Contract contract;
  private Person person ;
  private List<BillDetail> billDetails = new ArrayList<>();
  private List<ServiceFee> serviceFees = new ArrayList<>();


  @BeforeEach
  void setup(){
    // bill
    bill.setStauts(false);
    bill.setId(1l);

    // add serviceFee
    ServiceFee serviceFee1 = new ServiceFee(1l, Double.valueOf(2000), "vnd/m3", TypeService.WATER);
    serviceFee1.setId(1l);
    ServiceFee serviceFee2 = new ServiceFee(2l, Double.valueOf(4000), "vnd/num", TypeService.ELECTRICITY);
    serviceFee2.setId(2l);
    serviceFees.addAll(List.of(serviceFee1, serviceFee2));


    // add billDetail
    BillDetail billDetail = new BillDetail(1l, 0d, 50d, bill, serviceFees.get(0));
    billDetail.setId(1l);
    billDetail.setSubTotal(billDetail.getConsume()*serviceFees.get(0).getPrice());

    BillDetail billDetail1 = new BillDetail(2l, 0d, 20d, bill, serviceFees.get(1));
    billDetail1.setId(2l);
    billDetail1.setSubTotal(billDetail.getConsume()*serviceFees.get(1).getPrice());

    bill.setBillDetailList(List.of(billDetail, billDetail1));


    // person
//    contract = new Contract(1l, )
  }

  @Test
  void testAdd(){
    Long apartmentId = 1l;
    BillRequest billRequest = new BillRequest();
    billRequest.setBillDetailList(billDetails);
    billRequest.setNote("not paid");
    billRequest.setApartmentId(apartmentId);
    billRequest.setTermPayment(Date.valueOf("2023-04-25"));
    billRequest.setPaidDate(Date.valueOf("2023-04-30"));
    when(billRepo.save(bill)).thenReturn(bill);
//    when(contractRepo.findContractByApartmentId(apartmentId)).thenReturn();
    Bill bill1 = billService.add(billRequest);

    Assertions.assertNotNull(bill1);
    assertEquals(bill1.getBillDetailList(), billDetails);
    assertEquals(bill1.getStauts(), false);
    assertEquals(bill1.getNote(), "not paid");
    assertEquals(bill1.getTotal(),
        billDetails.get(0).getSubTotal() + billDetails.get(1).getSubTotal());
    assertEquals(bill1.getTermPayment(), Date.valueOf("2023-04-25"));
    assertEquals(bill1.getPaidDate(), Date.valueOf("2023-04-30"));

  }
}
