package com.vmo.apartment_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.vmo.apartment_manager.controller.ServiceFeeController;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import com.vmo.apartment_manager.payload.response.ServiceFeeResponse;
import com.vmo.apartment_manager.service.ServiceFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ServiceFeeControllerTest {

  @Mock
  private ServiceFeeService feeService;

  @InjectMocks
  private ServiceFeeController serviceFeeController;



  @Test
  void testAdd(){
    ServiceFee serviceFee = new ServiceFee(1l, Double.valueOf(2000), "vnd/m3", TypeService.WATER);
    when(feeService.add(serviceFee)).thenReturn(new ServiceFeeResponse(serviceFee));
    ResponseEntity<?> response = serviceFeeController.add(serviceFee);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(new ServiceFeeResponse(serviceFee), response.getBody());

  }
}
