package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.service.ApartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apartments")
@SecurityRequirement(name = "Authorization")
public class ApartmentController {

  @Autowired
  ApartmentService apartmentService;

  @GetMapping()
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy) {
    return ResponseEntity.ok(apartmentService.getAll(pageNo, pageSize, sortBy));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getApartmentById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(apartmentService.findById(id));
  }

  @GetMapping("/search-by-name")
  public ResponseEntity<?> getApartmentByName(@RequestParam String apartmentName,
      @RequestParam(defaultValue = "1") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy) {
    return ResponseEntity.ok(apartmentService.findApartmentByName(apartmentName, pageNo, pageSize, sortBy));
  }

  @PostMapping("/search-by-represent")
  public ResponseEntity<?> getApartmentByRepresentPerson(@RequestBody Person person){
    return ResponseEntity.ok(apartmentService.findApartmentByRepresent(person.getFullName()));
  }

  @GetMapping("/count-apartment-occupied")
  public ResponseEntity<?> countApartmentInUse(){
    return ResponseEntity.ok(apartmentService.countApartmentInUse());
  }

  @GetMapping("/available")
  public ResponseEntity<?> getApartmentAvailable(){
    return ResponseEntity.ok(apartmentService.getApartmentsAvailable());
  }

  @GetMapping("/un-available")
  public ResponseEntity<?> getApartmentUnAvailable(){
    return ResponseEntity.ok(apartmentService.getApartmentsUnAvailable());
  }
}
