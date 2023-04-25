package com.vmo.apartment_manager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ContractStatus;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.request.PersonRequest;
import com.vmo.apartment_manager.payload.response.PersonResponse;
import com.vmo.apartment_manager.repository.ApartmentRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.impl.ApartmentServiceImpl;
import com.vmo.apartment_manager.service.impl.ContractServiceImpl;
import com.vmo.apartment_manager.service.impl.PersonServiceImpl;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

  @Mock
  private PersonRepository personRepo;

  @Mock
  private ApartmentRepository apartmentRepo;

  @Mock
  private ContractRepository contractRepo;

  @InjectMocks
  private PersonServiceImpl personService;

  @InjectMocks
  private ApartmentServiceImpl apartmentService;

  @InjectMocks
  private ContractServiceImpl contractService;

  private Person person = new Person(1l, "Nguyễn văn a", null, "09456789", "abc@gmail.com", "123456789", true, "it", null, false);
  private Apartment apartment = new Apartment(1l, "CT01", "CT01", 1000d, false, "good");
  private Contract contract = new Contract(1l, 9999d, Date.valueOf("2023-05-06"), Date.valueOf("2023-08-30"), "A01", ContractStatus.ACTIVE, apartment, person);
  List<Person> personList = new ArrayList<>();

  @BeforeEach
  void setup() {

    // person
    Person person = Person.builder()
        .id(1l)
        .status(null)
        .contractId(1l)
        .phone("123456789")
        .email("abc@gmail.com")
        .gender(true)
        .carrer(null)
        .dob(null)
        .cin("888888888")
        .fullName("nguyễn văn a")
        .build();

    Person person1 = Person.builder()
        .id(2l)
        .status(false)
        .phone("123456789")
        .email("abc@gmail.com")
        .gender(true)
        .contractId(2l)
        .carrer("laywer")
        .dob(Date.valueOf("1999-04-05"))
        .cin("999999999")
        .fullName("nguyễn văn b")
        .build();

    personList.addAll(List.of(person, person1));
  }

  @Test
  void testAdd(){
    PersonRequest request = new PersonRequest();
    request.setCin("888888888");
    request.setFullName("nguyễn văn a");
    request.setEmail("abc@gmail.com");
    request.setPhone("123456789");
    when(personRepo.save(any(Person.class))).thenReturn(new Person());

    PersonResponse person = personService.add(request);
    Assertions.assertNotNull(person);
    assertEquals(person.getFullName(), personList.get(0).getFullName());
    assertEquals(person.getCin(), personList.get(0).getCin());
    assertEquals(person.getPhone(), personList.get(0).getPhone());
    assertEquals(person.getEmail(), personList.get(0).getEmail());
  }

  @Test
  void testUpdate(){
    Long personId = 1l;
    Person person1 = Person.builder()
        .status(false)
        .phone("123456789")
        .email("abc@gmail.com")
        .gender(true)
        .carrer("laywer")
        .dob(Date.valueOf("1999-04-05"))
        .cin("999999999")
        .fullName("nguyễn văn b")
        .build();
    when(personRepo.findById(personId)).thenReturn(Optional.of(personList.get(0)));
    PersonResponse response = personService.update(personId, person1);
    Assertions.assertNotNull(response);
    verify(personRepo).save(person1);
  }
  @Test
  void testUpdateNotFound(){
    Long personId = 1l;
    Person person1 = Person.builder()
        .status(false)
        .phone("123456789")
        .email("abc@gmail.com")
        .gender(true)
        .carrer("laywer")
        .dob(Date.valueOf("1999-04-05"))
        .cin("999999999")
        .fullName("nguyễn văn b")
        .build();
    when(personRepo.findById(personId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.update(personId, person1));
    verify(personRepo).findById(personId);
    assertEquals(ConstantError.PERSON_NOT_FOUND + personId, exception.getMessage());
  }

  @Test
  void testGetPersonActiveByApartmentId(){
    Long apartmentId = 1l;
    Long contractId = 1l;
    when(apartmentRepo.findById(apartmentId)).thenReturn(Optional.of(apartment));
    when(personRepo.findAllByContractId(contractId)).thenReturn(personList);
    when(contractRepo.findContractByApartmentId(apartmentId)).thenReturn(Optional.of(contract));

    List<PersonResponse> responses = personService.getPersonsActiveByApartmentId(apartmentId);
    Assertions.assertNotNull(responses);
    assertEquals(responses.size(), 2);
  }

  @Test
  void testGetPersonActiveByApartmentIdNotFoundApartment(){
    Long apartmentId = 1l;
    when(apartmentRepo.findById(apartmentId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.getPersonsActiveByApartmentId(apartmentId));
    verify(apartmentRepo).findById(apartmentId);
    assertEquals(ConstantError.APARTMENT_NOT_FOUND + apartmentId, exception.getMessage());
  }

  @Test
  void testGetPersonActiveByApartmentIdNotFoundContract(){
    Long apartmentId = 1l;
    when(apartmentRepo.findById(apartmentId)).thenReturn(Optional.of(apartment));
    when(contractRepo.findContractByApartmentId(apartmentId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.getPersonsActiveByApartmentId(apartmentId));
    verify(contractRepo).findContractByApartmentId(apartmentId);
    assertEquals(ConstantError.CONTRACT_NOT_EXISTS_IN_APARTMENT + apartmentId, exception.getMessage());
  }

  @Test
  void testdeletePersonsById(){
    long []personsId = {1l, 2l};
    when(personRepo.findById(1l)).thenReturn(Optional.of(personList.get(0)));
    when(personRepo.findById(2l)).thenReturn(Optional.of(personList.get(1)));
    String deleteSuccedd = personService.deletePersonsById(personsId);
    assertEquals(deleteSuccedd, "Delete succedd!");

  }

  @Test
  void testdeletePersonsByIdNotFound(){
    long []personsId = {1l, 2l};
    when(personRepo.findById(1l)).thenReturn(Optional.of(personList.get(0)));
    when(personRepo.findById(2l)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.deletePersonsById(personsId));
    assertEquals(ConstantError.PERSON_NOT_FOUND + 2l, exception.getMessage());
  }

  @Test
  void testGetAll(){
    when(personRepo.findAll()).thenReturn(personList);
    List<Person> persons = personService.findAll();
    verify(personRepo, times(1)).findAll();
    assertEquals(persons.size(), 2);
  }

  @Test
  void testGetPersonByName(){
    String personName = "nguyễn";
    Apartment apartment1 = new Apartment(2l, "CT02", "CT02", 1000d, true, "good");
    when(personRepo.findPersonByName(personName)).thenReturn(personList);
    when(apartmentRepo.findApartmentByContractId(1l)).thenReturn(Optional.of(apartment));
    when(apartmentRepo.findApartmentByContractId(2l)).thenReturn(Optional.of(apartment1));

    List<PersonResponse> personResponses = personService.getPersonByName(personName);

    Assertions.assertNotNull(personResponses);
    assertEquals(personResponses.size(), 2);
  }

  @Test
  void testGetPersonByNameNotFoundApartment(){
    String personName = "nguyễn";
    Apartment apartment1 = new Apartment(2l, "CT02", "CT02", 1000d, true, "good");
    when(personRepo.findPersonByName(personName)).thenReturn(personList);
    when(apartmentRepo.findApartmentByContractId(1l)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.getPersonByName(personName));
    assertEquals(ConstantError.APARTMENT_NOT_FOUND , exception.getMessage());

  }

  @Test
  void testGetPersonByApartmentCode(){
    String apartmentCode = "CT01";
    when(contractRepo.findContractByApartmentCode(apartmentCode)).thenReturn(Optional.of(contract));
    when(personRepo.findPersonByContractId(1l)).thenReturn(personList);
    List<Person> persons = personService.findPersonsByApartmentCode(apartmentCode);
    Assertions.assertNotNull(persons);
    assertEquals(personList.size(), 2);
  }

  @Test
  void testGetPersonByApartmentCodeNotFoundContract(){
    String apartmentCode = "CT01";
    when(contractRepo.findContractByApartmentCode(apartmentCode)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.findPersonsByApartmentCode(apartmentCode));
    assertEquals(ConstantError.CONTRACT_NOT_FOUND , exception.getMessage());
  }

  @Test
  void testFindById(){
    when(personRepo.findById(1l)).thenReturn(Optional.of(personList.get(0)));
    Person person1 = personService.findById(1l);
    assertEquals(person1, personList.get(0));
  }

  @Test
  void testFindByIdNotFound(){
    Long personId = 1l;
    when(personRepo.findById(personId)).thenReturn(Optional.empty());
    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> personService.findById(personId));
    assertEquals(ConstantError.PERSON_NOT_FOUND + personId , exception.getMessage());
  }
}
