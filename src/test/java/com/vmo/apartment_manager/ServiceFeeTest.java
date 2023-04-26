package com.vmo.apartment_manager;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.response.ServiceFeeResponse;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.impl.ServiceFeeServiceImpl;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceFeeTest {

  @Mock
  ServiceFeeRepository serviceFeeRepo;

  @InjectMocks
  ServiceFeeServiceImpl service;

  private List<ServiceFee> serviceFees = new ArrayList<>();

  @BeforeEach
  void setup() {
    ServiceFee serviceFee1 = new ServiceFee();
    serviceFee1.setId(1l);
    serviceFee1.setName(TypeService.WATER);
    serviceFee1.setPrice(Double.valueOf(2000));
    serviceFee1.setUnit("vnd/m3");
    ServiceFee serviceFee2 = new ServiceFee();
    serviceFee2.setId(1l);
    serviceFee2.setName(TypeService.ELECTRICITY);
    serviceFee2.setPrice(Double.valueOf(5000));
    serviceFee2.setUnit("vnd/m3");
    serviceFees.addAll(List.of(serviceFee1, serviceFee2));
  }

  @AfterEach
  void setupAfter() {
    serviceFeeRepo.deleteAll();
  }

  // Test method add serviceFee
  @Test
  void testAddServiceFee() {
    when(serviceFeeRepo.save(serviceFees.get(0))).thenReturn(serviceFees.get(0));
    ServiceFeeResponse serviceFee = service.add(serviceFees.get(0));
    Assertions.assertNotNull(serviceFee);
    assertEquals(serviceFee.getUnit(), serviceFees.get(0).getUnit());
    assertEquals(serviceFee.getName(), serviceFees.get(0).getName());
    assertEquals(serviceFee.getPrice(), serviceFees.get(0).getPrice());
  }

  @Test
  void testUpdateService() {
    when(serviceFeeRepo.findById(1l)).thenReturn(Optional.of(serviceFees.get(0)));
    ServiceFee serviceFee1 = new ServiceFee();
    serviceFee1.setId(1l);
    serviceFee1.setName(TypeService.WATER);
    serviceFee1.setPrice(Double.valueOf(50000));
    serviceFee1.setUnit("vnd/m3");
    when(serviceFeeRepo.save(serviceFee1)).thenReturn(serviceFee1);
    ServiceFeeResponse response = service.update(1l, serviceFee1);
    Assertions.assertNotNull(response);
    assertEquals(response.getPrice(), Double.valueOf(50000));
  }


  @Test
  void testGetAll() {
    when(serviceFeeRepo.findAll()).thenReturn(serviceFees);
    List<ServiceFee> serviceFeeList = service.getAll();
    assertEquals(serviceFeeList, serviceFees);
    verify(serviceFeeRepo, times(1)).findAll();
  }

  @Test
  void testFindById(){
    when(serviceFeeRepo.findById(1l)).thenReturn(Optional.of(serviceFees.get(0)));
    ServiceFeeResponse serviceFee1 = service.findById(1l);
    assertEquals(serviceFee1.getPrice(), serviceFees.get(0).getPrice());
    assertEquals(serviceFee1.getName(), serviceFees.get(0).getName());
    assertEquals(serviceFee1.getUnit(), serviceFees.get(0).getUnit());
    verify(serviceFeeRepo, times(1)).findById(1l);
  }

  @Test
  void deleteById(){
    when(serviceFeeRepo.findById(1l)).thenReturn(Optional.of(serviceFees.get(0)));
    service.deleteById(1l);
    verify(serviceFeeRepo, times(1)).delete(serviceFees.get(0));
  }

  @Test
  void testFindByIdNotFound(){
    Long ServiceFeeId = 1l;
    when(serviceFeeRepo.findById(ServiceFeeId)).thenThrow(new NotFoundException(ConstantError.SERVICE_NOT_EXISTS + ServiceFeeId));
    assertThrows(NotFoundException.class, () -> {
      service.findById(ServiceFeeId);
    });
    verify(serviceFeeRepo, times(1)).findById(ServiceFeeId);
  }

  @Test
  void deleteServiceFeeNotFound(){
    Long serviceFeeId = 1l;
    when(serviceFeeRepo.findById(serviceFeeId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      service.deleteById(serviceFeeId);
    });
  }

  @Test
  void testUpdateServiceFeeNotFound(){
    Long serviceFeeId = 1l;
    ServiceFee serviceFee1 = new ServiceFee();
    serviceFee1.setId(1l);
    serviceFee1.setName(TypeService.WATER);
    serviceFee1.setPrice(Double.valueOf(50000));
    serviceFee1.setUnit("vnd/m3");
    when(serviceFeeRepo.findById(serviceFeeId)).thenReturn(Optional.empty());

    // act
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> service.update(serviceFeeId, serviceFee1));
    verify(serviceFeeRepo).findById(serviceFeeId);
    assertEquals(ConstantError.SERVICE_NOT_EXISTS + serviceFeeId, exception.getMessage());
  }




}
