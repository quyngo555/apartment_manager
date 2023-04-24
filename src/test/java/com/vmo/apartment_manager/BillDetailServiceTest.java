package com.vmo.apartment_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.response.BillDetailResponse;
import com.vmo.apartment_manager.payload.response.ServiceFeeResponse;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.BillDetailService;
import com.vmo.apartment_manager.service.BillService;
import com.vmo.apartment_manager.service.impl.BillDetailServiceImpl;
import com.vmo.apartment_manager.service.impl.BillServiceImpl;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BillDetailServiceTest {

  @Mock
  BillDetailRepository billDetailRepo;

  @Mock
  ServiceFeeRepository serviceFeeRepo;

  @Mock
  BillRepository billRepo;

  @InjectMocks
  BillDetailServiceImpl billDetailService;

  @InjectMocks
  BillServiceImpl billService;



  private BillDetail billDetail;
  private List<ServiceFee> serviceFees = new ArrayList<>();
  private Bill bill;

  @BeforeEach
  void setup(){

    ServiceFee serviceFee = new ServiceFee();
    serviceFee.setId(1l);
    serviceFee.setName(TypeService.WATER);
    serviceFee.setPrice(Double.valueOf(2000));
    serviceFee.setUnit("vnd/m3");

    ServiceFee serviceFee2 = new ServiceFee();
    serviceFee2.setId(2l);
    serviceFee2.setName(TypeService.ELECTRICITY);
    serviceFee2.setPrice(Double.valueOf(5000));
    serviceFee2.setUnit("vnd/num");

    serviceFees.addAll(List.of(serviceFee, serviceFee2));

    bill = new Bill();
    bill.setId(1l);
    bill.setTotal(0d);
    bill.setBillDetailList(new ArrayList<>());
    bill.setTermPayment(Date.valueOf("2023-04-15"));
    bill.setStauts(false);

    bill.setStauts(false);

    billDetail = new BillDetail();
    billDetail.setId(1l);
    billDetail.setServiceFee(serviceFees.get(0));
    billDetail.setConsume(50d);
    billDetail.setSubTotal(billDetail.getConsume()*serviceFee.getPrice());
    billDetail.setBill(bill);
  }

  @AfterEach
  void tearDown(){
    billDetailRepo.deleteAll();
  }

  @Test
  void testAdd(){
    when(serviceFeeRepo.findById(1l)).thenReturn(Optional.of(serviceFees.get(0)));
    when(billDetailRepo.save(billDetail)).thenReturn(billDetail);
    when(billRepo.findById(1l)).thenReturn(Optional.of(bill));

    BillDetailResponse billDetail1 = billDetailService.add(billDetail);
    Bill bill1 = billService.update(bill.getId(), bill);
    Assertions.assertNotNull(billDetail1);
    assertEquals(billDetail1.getConsume(), billDetail.getConsume());
    assertEquals(billDetail1.getSubTotal(), billDetail.getSubTotal());
    assertEquals(billDetail1.getServiceFee().getName(), billDetail.getServiceFee().getName());
    assertEquals(billDetail1.getServiceFee().getUnit(), billDetail.getServiceFee().getUnit());
    assertEquals(billDetail1.getServiceFee().getPrice(), billDetail.getServiceFee().getPrice());
  }

  @Test
  void testAddServiceFeeNotFound(){
    Long serviceFeeId = 1l;
    when(serviceFeeRepo.findById(serviceFeeId)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> billDetailService.add(billDetail));
    verify(serviceFeeRepo).findById(serviceFeeId);
    assertEquals(ConstantError.SERVICE_NOT_EXISTS + serviceFeeId, exception.getMessage());
  }

  @Test
  void testAddBillNotFound(){
    Long billId = 1l;
    when(billRepo.findById(billId)).thenReturn(Optional.empty());
    when(serviceFeeRepo.findById(1l)).thenReturn(Optional.of(serviceFees.get(0)));
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> billDetailService.add(billDetail));
    verify(billRepo).findById(billId);
    assertEquals(ConstantError.BILL_NOT_FOUND + billId, exception.getMessage());
  }



  @Test
  void testUpdate(){
    BillDetail billDetailUpdate = new BillDetail();
    billDetailUpdate.setId(1l);
    billDetailUpdate.setServiceFee(serviceFees.get(0));
    billDetailUpdate.setConsume(20d);
    billDetailUpdate.setSubTotal(billDetailUpdate.getConsume()*serviceFees.get(0).getPrice());
    billDetailUpdate.setBill(bill);
    when(billDetailRepo.save(billDetailUpdate)).thenReturn(billDetailUpdate);
    when(billRepo.findById(1l)).thenReturn(Optional.of(bill));
    when(billDetailRepo.findById(1l)).thenReturn(Optional.of(billDetail));
    BillDetailResponse billDetail1 = billDetailService.update(billDetail.getId(), billDetailUpdate);

    Assertions.assertNotNull(billDetail1);
    assertEquals(billDetail1.getConsume(), billDetailUpdate.getConsume());
    assertEquals(billDetail1.getSubTotal(), billDetailUpdate.getSubTotal());
  }

  @Test
  void testUpdateNotFound(){
    BillDetail billDetailUpdate = new BillDetail();
    billDetailUpdate.setId(1l);
    billDetailUpdate.setServiceFee(serviceFees.get(0));
    billDetailUpdate.setConsume(20d);
    billDetailUpdate.setSubTotal(billDetailUpdate.getConsume()*serviceFees.get(0).getPrice());
    billDetailUpdate.setBill(bill);
    Long billDetailId = 1l;
    when(billDetailRepo.findById(billDetailId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> billDetailService.update(billDetailId, billDetailUpdate));
    verify(billDetailRepo).findById(billDetailId);
    assertEquals(ConstantError.BILL_DETAIL_NOT_FOUND + billDetailId, exception.getMessage());
  }

  @Test
  void testFindById(){
    when(billDetailRepo.findById(1l)).thenReturn(Optional.of(billDetail));
    BillDetailResponse billDetail1 = billDetailService.findById(1l);
    assertNotNull(billDetail1);
    verify(billDetailRepo, times(1)).findById(1l);
  }

  @Test
  void testFindByIdNotFound(){
    Long billDetailId = 1l;
    when(billDetailRepo.findById(billDetailId)).thenThrow(new NotFoundException(
        ConstantError.BILL_NOT_FOUND + billDetailId));
    assertThrows(NotFoundException.class, () -> {
      billDetailService.findById(billDetailId);
    });
    verify(billDetailRepo, times(1)).findById(billDetailId);
  }

  @Test
  void testGetAll() {
    BillDetail billDetail1 = new BillDetail();
    billDetail1.setId(2l);
    billDetail1.setServiceFee(serviceFees.get(1));
    billDetail1.setConsume(70d);
    billDetail1.setSubTotal(billDetail.getConsume()*serviceFees.get(1).getPrice());
    billDetail1.setBill(billDetail.getBill());

    when(billDetailRepo.findAll()).thenReturn(List.of(billDetail, billDetail1));
    List<BillDetailResponse> billDetails = billDetailService.getAll();
    Assertions.assertNotNull(billDetails);
    verify(billDetailRepo, times(1)).findAll();
  }
}