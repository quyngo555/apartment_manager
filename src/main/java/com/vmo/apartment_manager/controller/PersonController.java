package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.service.PersonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "Authorization")
public class PersonController {

  @Autowired
  private PersonService personService;

  @GetMapping("/persons")
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    return ResponseEntity.ok(personService.getAll(pageNo, pageSize, sortBy));
  }
  @GetMapping("/persons/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(personService.findById(id));
  }
  @PostMapping("/persons")
  public ResponseEntity<?> add(@RequestBody Person person){
    return ResponseEntity.ok(personService.add(person));
  }
  @PutMapping("/persons/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Person person){
    return ResponseEntity.ok(personService.update(id, person));
  }
  @PutMapping("persons/{id}/change-status")
  public ResponseEntity<?> changeStatus(@PathVariable("id")long id){
    return ResponseEntity.ok(personService.deletePersonById(id));
  }
  @PutMapping("/persons/change-status")
  public ResponseEntity<?> changeAllStatus(@RequestBody long []ids){
    return ResponseEntity.ok(personService.deletePersonsById(ids));
  }
  @GetMapping("/apartments/{id}/persons")
  public ResponseEntity<?> getAllByApartmentId(@PathVariable("id") long id,@RequestParam(defaultValue = "1") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    return ResponseEntity.ok(personService.getAllByApartmentId(id, pageNo, pageSize, sortBy));
  }

  @GetMapping("/apartments/{id}/persons-active")
  public ResponseEntity<?> getPersonsActiveByApartmentId(@PathVariable("id") long id){
    return ResponseEntity.ok(personService.getPersonsActiveByApartmentId(id));
  }

  @GetMapping("/apartments/{id}/persons-un-active")
  public ResponseEntity<?> getPersonsUnActiveByApartmentId(@PathVariable("id") long id, @RequestParam(defaultValue = "0") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    return ResponseEntity.ok(personService.getPersonsUnActiveByApartmentId(id, pageNo, pageSize, sortBy));
  }

  @PostMapping("/persons/search-by-name")
  public ResponseEntity<?> getPersonByName(@RequestBody Person p){
    return ResponseEntity.ok(personService.getPersonByName(p.getFullName()));
  }

  @GetMapping("/persons/represent")
  public ResponseEntity<?> getRepresent(){
    return ResponseEntity.ok(personService.getRepresent());
  }
  @GetMapping("/persons/search-by-date")
  public ResponseEntity<?> getPersonCreatedBetween(@RequestParam Date startDate,
      @RequestParam Date endDate) {
    return ResponseEntity.ok(personService.findPersonByCreatedBetween(startDate, endDate));
  }
}
