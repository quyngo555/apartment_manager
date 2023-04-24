package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.service.ServiceFeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<?> addBill(@Valid @RequestBody ServiceFee serviceFee, BindingResult result){
    if(result.hasErrors()){
      Map<String, String> errors = new HashMap<>();
      result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
      return ResponseEntity.badRequest().body(errors);
    }
    return ResponseEntity.ok(service.add(serviceFee));
  }
  @PutMapping("/{id}")
  public ResponseEntity<?> updateBill(@PathVariable("id") long id,@RequestBody ServiceFee serviceFee){
    return ResponseEntity.ok(service.update(id, serviceFee));
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(service.findById(id));
  }
  @GetMapping("")
  public ResponseEntity<?> getAll(){
    return ResponseEntity.ok(service.getAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable("id") long id){
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
