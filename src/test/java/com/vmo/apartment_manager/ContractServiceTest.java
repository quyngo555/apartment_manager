package com.vmo.apartment_manager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.service.impl.ContractServiceImpl;
import java.sql.Date;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

  @Mock
  ContractRepository contractRepo;

  @InjectMocks
  ContractServiceImpl contractService;

  private Contract contract;
  private Apartment apartment;
  private Person person;

  @BeforeEach
  void setup(){
    contract = new Contract(1l, 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    person = new Person(1l, "Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    apartment = new Apartment(1l, "CT01", "CT01", 1000d, false, "good");
  }
  @Test
  void testFindById(){
    Long contractId = 1l;
    when(contractRepo.findById(contractId)).thenReturn(Optional.of(contract));
    Contract contract1 = contractService.findById(contractId);
    Assertions.assertNotNull(contract1);
    verify(contractRepo, times(1)).findById(contractId);
  }

  @Test
  void testFindByIdNotFound(){
    Long contractId = 1l;
    when(contractRepo.findById(contractId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.findById(contractId));
    verify(contractRepo).findById(contractId);
    Assertions.assertEquals(ConstantError.CONTRACT_NOT_FOUND + contractId, exception.getMessage());
  }

  @Test
  void testFindAll(){
   Contract contract1 = new Contract(1l, 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    Contract contract2 = new Contract(2l, 8888d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A02", ContractStatus.ACTIVE, apartment, person);
    Contract contract3 = new Contract(3l, 7777d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A03", ContractStatus.ACTIVE, apartment, person);
  }

}
