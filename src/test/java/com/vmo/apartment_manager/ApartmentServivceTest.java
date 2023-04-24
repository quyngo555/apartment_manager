package com.vmo.apartment_manager;

import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.payload.response.ApartmentResponse;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.impl.ApartmentServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
public class ApartmentServivceTest {

  @Mock
  private ApartmentRepository apartmentRepo;

  @Mock
  private PersonRepository personRepo;

  @InjectMocks
  private ApartmentServiceImpl apartmentService;

  @Test
  void testGetAll(){
    List<ApartmentResponse> list = apartmentService.getAll(1, 2, "id");
    verify(apartmentRepo).findAll();
    Assertions.assertNotNull(list);
  }
}
