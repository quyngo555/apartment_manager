package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.service.BillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api")
@SecurityRequirement(name = "Authorization")
public class BillController {

  @Autowired
  BillService billService;

  @PostMapping("/bills")
  public ResponseEntity<?> add(@RequestBody Bill bill) {
    return ResponseEntity.ok(billService.add(bill));
  }

  @PutMapping("/bills/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Bill bill) {
    return ResponseEntity.ok(billService.update(id, bill));
  }
  @GetMapping("/bills")
  public ResponseEntity<?> getAll(){
    return ResponseEntity.ok(billService.findAll());
  }
  @GetMapping("/bills/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(billService.findById(id));
  }
}
