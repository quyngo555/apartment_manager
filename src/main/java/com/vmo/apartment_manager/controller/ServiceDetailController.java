package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.service.ServiceFeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-fee")
@SecurityRequirement(name = "Authorization")
public class ServiceDetailController {

  @Autowired
  ServiceFeeService service;

  @PostMapping("")
  public ResponseEntity<?> addBill(@RequestBody ServiceFee serviceFee){
    return ResponseEntity.ok(service.add(serviceFee));
  }
  @PutMapping("/{id}")
  public ResponseEntity<?> updateBill(@PathVariable("id") long id, @RequestBody ServiceFee serviceFee){
    return ResponseEntity.ok(service.update(id, serviceFee));
  }

}
