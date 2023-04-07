package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.service.ApartmentService;
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
@RequestMapping("/api/apartments")
public class ApartmentController {

  @Autowired
  ApartmentService apartmentService;

  @GetMapping()
  public ResponseEntity<?> getAll(){
    return ResponseEntity.ok(apartmentService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getApartmentById(@PathVariable("id") Long id){
    return ResponseEntity.ok(apartmentService.findById(id));
  }

  @PostMapping("")
  public ResponseEntity<?> addApartment(@RequestBody Apartment apartment){
    return ResponseEntity.ok(apartmentService.add(apartment));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateApartment(@PathVariable("id") long id, @RequestBody Apartment apartment){
    return ResponseEntity.ok(apartmentService.update(id, apartment));
  }


}
