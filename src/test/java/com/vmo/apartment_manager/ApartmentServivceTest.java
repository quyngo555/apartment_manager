package com.vmo.apartment_manager;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.payload.response.ApartmentResponse;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.impl.ApartmentServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
public class ApartmentServivceTest {

  @Mock
  private ApartmentRepository apartmentRepo;

  @Mock
  private PersonRepository personRepo;

  @InjectMocks
  private ApartmentServiceImpl apartmentService;

  private Apartment apartment = new Apartment(1l, "CT01", "CT01", 1000d, false, "good");
  private List<Contract> contracts = new ArrayList<>();
  private List<Person> persons = new ArrayList<>();
  @BeforeEach
  void setup(){
    Person person = new Person(1l, "Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    Person person1 = new Person(2l, "Nguyễn văn b", null, "09456789", "ccc@gmail.com", "123456789", true, "it", null, false);
    persons.addAll(List.of(person, person1));
    Contract contract = new Contract(1l, 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    Contract contract1 = new Contract(2l, 8888, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A02", ContractStatus.ACTIVE, apartment, person);
    contracts.addAll(List.of(contract1, contract));
  }

  @Test
  void testGetAll(){
    Pageable paging = PageRequest.of(0, 2, Sort.by("id"));
    Apartment apartment1 = new Apartment(2l, "CT02", "CT02", 1000d, false, "good");
    when(apartmentRepo.findAll()).thenReturn(List.of(apartment1, apartment));
    List<ApartmentResponse> list = apartmentService.getAll(1, 2, "id");
    verify(apartmentRepo).findAll();
    Assertions.assertNotNull(list);
  }
}
