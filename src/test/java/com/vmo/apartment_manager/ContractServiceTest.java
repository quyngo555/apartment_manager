package com.vmo.apartment_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.request.ContractRequest;
import com.vmo.apartment_manager.payload.response.ContractResponse;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.impl.ContractServiceImpl;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

  @Mock
  ContractRepository contractRepo;

  @Mock
  ApartmentRepository apartmentRepo;

  @Mock
  PersonRepository personRepo;

  @InjectMocks
  ContractServiceImpl contractService;

  private Contract contract;
  private Apartment apartment;
  private Person person;

  @BeforeEach
  void setup() {
    person = new Person(1l, "Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true,
        "it", null, false);
    apartment = new Apartment(1l, "CT01", "CT01", 1000d, false, "good");
    contract = new Contract(1l, 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"),
        "A01", ContractStatus.ACTIVE, apartment, person);


  }

  @Test
  void testFindById() {
    Long contractId = 1l;
    when(contractRepo.findById(contractId)).thenReturn(Optional.of(contract));
    Contract contract1 = contractService.findById(contractId);
    Assertions.assertNotNull(contract1);
    verify(contractRepo, times(1)).findById(contractId);
  }

  @Test
  void testFindByIdNotFound() {
    Long contractId = 1l;
    when(contractRepo.findById(contractId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.findById(contractId));
    verify(contractRepo).findById(contractId);
    Assertions.assertEquals(ConstantError.CONTRACT_NOT_FOUND + contractId, exception.getMessage());
  }

  @Test
  void testFindAll() {
    Contract contract1 = new Contract(1l, 9999d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    Contract contract2 = new Contract(2l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A02", ContractStatus.ACTIVE, apartment, person);
    Contract contract3 = new Contract(3l, 7777d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A03", ContractStatus.ACTIVE, apartment, person);
    Pageable paging = PageRequest.of(0, 3, Sort.by("id"));
    when(contractRepo.findContractActiveWithPagination(paging)).thenReturn(List.of(contract1, contract2, contract3));
    List<ContractResponse> contracts = contractService.getAllContractActive(1, 3, "id");
    Assertions.assertNotNull(contracts);
    verify(contractRepo, times(1)).findContractActiveWithPagination(paging);
  }

  @Test
  void testAdd() throws Exception {
    ContractRequest request = new ContractRequest(9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-05-06"), "CT01", ContractStatus.ACTIVE, apartment, person, true);
    when(contractRepo.save(any(Contract.class))).thenReturn(new Contract());
    when(apartmentRepo.findById(1l)).thenReturn(Optional.of(apartment));
    when(personRepo.findById(1l)).thenReturn(Optional.of(person));
    when(contractRepo.checkContractActiveByApartmentId(1l)).thenReturn(0);
    ContractResponse contract1 = contractService.add(request);
    Assertions.assertNotNull(contract1);
    Assertions.assertEquals(contract1.getPerson().getId(), person.getId());
    Assertions.assertEquals(contract1.getPriceApartment(), contract.getPriceApartment());
    Assertions.assertEquals(contract1.getApartment(), apartment);
  }

  @Test
  void testAddNotFoundApartment() throws Exception {
    ContractRequest request = new ContractRequest(9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-05-06"), "CT01", ContractStatus.ACTIVE, apartment, person, true);
    when(apartmentRepo.findById(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.add(request));
    verify(apartmentRepo).findById(1l);
    Assertions.assertEquals(ConstantError.APARTMENT_NOT_FOUND, exception.getMessage());
  }

  @Test
  void testAddLackOfPhoneOrEmail(){
    ContractRequest request = new ContractRequest(9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-05-06"), "CT01", ContractStatus.ACTIVE, apartment, person, true);
    when(apartmentRepo.findById(1l)).thenReturn(Optional.of(apartment));
    when(contractRepo.checkContractActiveByApartmentId(1l)).thenReturn(1);

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.add(request));
    verify(contractRepo).checkContractActiveByApartmentId(1l);
    Assertions.assertEquals(ConstantError.CONTRACT_EXISTS, exception.getMessage());
  }

  @Test
  void testAddNotFoundPerson(){
    ContractRequest request = new ContractRequest(9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-05-06"), "CT01", ContractStatus.ACTIVE, apartment, person, true);
    when(apartmentRepo.findById(1l)).thenReturn(Optional.of(apartment));
    when(personRepo.findById(1l)).thenReturn(Optional.empty());
    when(contractRepo.checkContractActiveByApartmentId(1l)).thenReturn(0);

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.add(request));
    verify(personRepo).findById(1l);
    Assertions.assertEquals(ConstantError.PERSON_NOT_FOUND, exception.getMessage());
  }

  @Test
  void testChangeRepresent(){
    Person person1 = new Person(3l,"Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    Person person2 = new Person(2l,"Nguyễn văn b", null, "09456788", "abc@gmail.com", "123456788", false, "it", null, false);
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findRepresentByContractId(1l)).thenReturn(Optional.of(person));
    when(apartmentRepo.findApartmentByContractId(1l)).thenReturn(Optional.of(apartment));
    when(personRepo.findPersonByContractId(1l)).thenReturn(List.of(person1, person2));
    when(personRepo.findById(2l)).thenReturn(Optional.of(person2));
    when(contractRepo.save(any(Contract.class))).thenReturn(new Contract());
    Contract contract1 = contractService.changeRepersent(1l, 2l);
    Assertions.assertNotNull(contract1);
    assertEquals(contract1.getPriceApartment(), contract.getPriceApartment());
    assertEquals(contract1.getApartment(), contract.getApartment());
    assertEquals(contract1.getPerson().getId(), person2.getId());
    verify(contractRepo, times(2)).save(any(Contract.class));
  }

  @Test
  void testchangeRepresentNotFoundContract(){
    Long contractId = 1l;
    Long newPresent = 1l;
    when(contractRepo.findById(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.changeRepersent(contractId, newPresent));
    verify(contractRepo).findById(contractId);
    Assertions.assertEquals(ConstantError.CONTRACT_NOT_FOUND + contractId, exception.getMessage());
  }

  @Test
  void testChangeRepresentNotFoundRepresentOld(){
    Long contractId = 1l;
    Long newPresent = 1l;
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findRepresentByContractId(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.changeRepersent(contractId, newPresent));
    verify(personRepo).findRepresentByContractId(1l);
    Assertions.assertEquals(ConstantError.PERSON_NOT_FOUND , exception.getMessage());
  }

  @Test
  void testChangeRepresentNotFoundApartment(){
    Long contractId = 1l;
    Long newPresent = 1l;
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findRepresentByContractId(1l)).thenReturn(Optional.of(person));
    when(apartmentRepo.findApartmentByContractId(1l)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.changeRepersent(contractId, newPresent));
    verify(personRepo).findRepresentByContractId(1l);
    Assertions.assertEquals(ConstantError.APARTMENT_NOT_FOUND , exception.getMessage());
  }

  @Test
  void testChangeRepresentNotFoundNewRepresent(){
    Long contractId = 1l;
    Long newPresent = 2l;
    Person person1 = new Person(3l,"Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    Person person2 = new Person(2l,"Nguyễn văn b", null, "09456788", "abc@gmail.com", "123456788", false, "it", null, false);
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findRepresentByContractId(1l)).thenReturn(Optional.of(person));
    when(apartmentRepo.findApartmentByContractId(1l)).thenReturn(Optional.of(apartment));
    when(personRepo.findPersonByContractId(1l)).thenReturn(List.of(person1, person2));
    when(personRepo.findById(2l)).thenReturn(Optional.empty());
    when(contractRepo.save(any(Contract.class))).thenReturn(new Contract());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.changeRepersent(contractId, newPresent));
    verify(personRepo).findRepresentByContractId(1l);
    Assertions.assertEquals(ConstantError.PERSON_NOT_FOUND  , exception.getMessage());
  }

  @Test
  void testFindContractByCreatedBetween(){
    Person person1 = new Person(3l,"Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
    Person person2 = new Person(2l,"Nguyễn văn b", null, "09456788", "abc@gmail.com", "123456788", false, "it", null, false);
    Contract contract1 = new Contract(1l, 9999d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    Contract contract2 = new Contract(2l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A02", ContractStatus.ACTIVE, apartment, person1);
    Contract contract3 = new Contract(3l, 7777d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A03", ContractStatus.ACTIVE, apartment, person2);
    Date startDate = Date.valueOf("2023-04-20");
    Date endDate = Date.valueOf("2023-05-20");
    Pageable paging = PageRequest.of(0, 3, Sort.by("id"));
    when(contractRepo.findByCreatedDateBetweenDatesWithPagination(startDate, endDate, paging)).thenReturn(List.of(contract1, contract2,contract3));
    List<ContractResponse> responses = contractService.findContractByCreatedBetween(startDate, endDate, 1, 3, "id");
    Assertions.assertNotNull(responses);
    assertEquals(responses.size(), 3);
  }

  @Test
  void testchangeAllStatusByIds(){
    Contract contract1 = new Contract(1l, 9999d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    Contract contract2 = new Contract(2l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A02", ContractStatus.ACTIVE, apartment, person);
    long[] ids = {1l, 2l};
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract1));
    when(contractRepo.findById(2l)).thenReturn(Optional.of(contract2));
    String response = contractService.changeAllStatusByIds(ids);
    assertEquals(response, "Change status succedd!");
  }

  @Test
  void testchangeAllStatusByIdsNotFoundContract(){
    Contract contract1 = new Contract(1l, 9999d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    Contract contract2 = new Contract(2l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A02", ContractStatus.ACTIVE, apartment, person);
    long[] ids = {1l, 2l};
    when(contractRepo.findById(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.changeAllStatusByIds(ids));
    verify(contractRepo).findById(1l);
    Assertions.assertEquals(ConstantError.CONTRACT_NOT_FOUND  , exception.getMessage());
  }

  @Test
  void testFindContractByApartmentId(){
    Long apartmentId = 1l;
    when(contractRepo.findContractByApartmentId(apartmentId)).thenReturn(Optional.of(contract));
    ContractResponse response = contractService.findContractByApartmentId(apartmentId);
    Assertions.assertNotNull(response);
    assertEquals(response.getApartment().getId(), contract.getApartment().getId());
    assertEquals(response.getPerson().getId(), contract.getPerson().getId());
  }

  @Test
  void testFindContractByNotFoundApartmentId(){
    Long apartmentId = 1l;
    when(contractRepo.findContractByApartmentId(apartmentId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.findContractByApartmentId(apartmentId));
    verify(contractRepo).findContractByApartmentId(apartmentId);
    Assertions.assertEquals(ConstantError.CONTRACT_NOT_FOUND  , exception.getMessage());
  }

  @Test
  void testUpdate(){
    Contract contract1 = new Contract(1l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findById(1l)).thenReturn(Optional.of(person));
    when(apartmentRepo.findById(1l)).thenReturn(Optional.of(apartment));
    Contract contract2 = contractService.update(1l, contract1);
    Assertions.assertNotNull(contract2);
    verify(contractRepo).save(contract1);
  }

  @Test
  void testUpdateNotFound(){
    Long contractId = 1l;
    Contract contract1 = new Contract(1l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    when(contractRepo.findById(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.update(contractId, contract1));
    verify(contractRepo).findById(contractId);
    Assertions.assertEquals(ConstantError.CONTRACT_NOT_FOUND + contractId, exception.getMessage());
  }

  @Test
  void testUpdateNotFoundApartment(){
    Long contractId = 1l;
    Long apartmentId = 1l;
    Contract contract1 = new Contract(1l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findById(1l)).thenReturn(Optional.of(person));
    when(apartmentRepo.findById(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.update(contractId, contract1));
    verify(apartmentRepo).findById(apartmentId);
    Assertions.assertEquals(ConstantError.APARTMENT_NOT_FOUND + apartmentId, exception.getMessage());

  }

  @Test
  void testUpdateNotFoundPerson(){
    Long contractId = 1l;
    Long personId = 1l;
    Contract contract1 = new Contract(1l, 8888d, Date.valueOf("2023-05-06"),
        Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
    when(contractRepo.findById(1l)).thenReturn(Optional.of(contract));
    when(personRepo.findById(1l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> contractService.update(contractId, contract1));
    verify(personRepo).findById(personId);
    Assertions.assertEquals(ConstantError.PERSON_NOT_FOUND + personId, exception.getMessage());

  }

}
