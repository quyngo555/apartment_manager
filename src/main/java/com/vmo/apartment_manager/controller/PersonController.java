package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.payload.request.PersonRequest;
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
@RequestMapping("/api")
@SecurityRequirement(name = "Authorization")
public class PersonController {

  @Autowired
  private PersonService personService;

  @PostMapping("/persons")
  public ResponseEntity<?> add(@RequestBody PersonRequest person){
    return ResponseEntity.ok(personService.add(person));
  }
  @GetMapping("/persons")
  public ResponseEntity<?> getAll(){
    return ResponseEntity.ok(personService.findAll());
  }
  @PutMapping("/persons/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Person person){
    return ResponseEntity.ok(personService.update(id, person));
  }
  @PutMapping("/persons/change-status")
  public ResponseEntity<?> changeAllStatus(@RequestBody long []ids){
    return ResponseEntity.ok(personService.deletePersonsById(ids));
  }

  @GetMapping("/apartments/{id}/persons")
  public ResponseEntity<?> getPersonsActiveByApartmentId(@PathVariable("id") long id){
    return ResponseEntity.ok(personService.getPersonsActiveByApartmentId(id));
  }

  @GetMapping("/persons/search-by-name")
  public ResponseEntity<?> getPersonByName(@RequestParam String personName){
    return ResponseEntity.ok(personService.getPersonByName(personName));
  }

  @GetMapping("/persons/represent")
  public ResponseEntity<?> getRepresent(@RequestParam(defaultValue = "1") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    return ResponseEntity.ok(personService.getRepresent(pageNo, pageSize, sortBy));
  }

  @GetMapping("/apartment/persons")
  public ResponseEntity<?> getPersonByApartmentCode(@RequestParam String apartmentCode){
    return ResponseEntity.ok(personService.findPersonsByApartmentCode(apartmentCode));
  }

@GetMapping("/persons/{id}")
  public ResponseEntity<?> findPersonById(@PathVariable("id") long id){
    return ResponseEntity.ok(personService.findById(id));
}


}
