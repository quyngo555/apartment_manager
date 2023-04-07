package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.PersonService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

  @Autowired
  PersonRepository personRepo;

  @Override
  public Person add(Person person) {

    return personRepo.save(person);
  }

  @Override
  public Person update(Long id, Person person) {
    Person person1= personRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
    person1.setPhone(person.getPhone());
    person1.setFullName(person.getFullName());
    person1.setIdParent(person1.getIdParent());
    person1.setDob(person.getDob());
    person1.setEmail(person.getEmail());
    person1.setCin(person.getCin());
    person1.setGender(person.getGender());
    person1.setCarrer(person.getCarrer());
    person1 = personRepo.save(person1);
    return person1;
  }

  @Override
  public List<Person> getAll() {
    return personRepo.findAll();
  }

  @Override
  public Person findById(Long id) {
    return personRepo.findById(id).orElseThrow(()->{
      throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
    });
  }

  @Override
  public List<Person> getAllByApartmentId(Long id) {
//    return personRepo.findAll();
    return null;
  }
}
