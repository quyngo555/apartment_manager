package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/persons")
public class PersonController {

  @Autowired
  private PersonService personService;

  @GetMapping("")
  public ResponseEntity<?> getAll(){
    return ResponseEntity.ok(personService.getAll());
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(personService.findById(id));
  }
  @PostMapping("")
  public ResponseEntity<?> add(@RequestBody Person person){
    return ResponseEntity.ok(personService.add(person));
  }
  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Person person){
    return ResponseEntity.ok(personService.update(id, person));
  }
}
