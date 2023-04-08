package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.service.ContractService;
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
@RequestMapping("/api/contracts")
public class ContractController {

  @Autowired
  ContractService contractService;

  @GetMapping("")
  public ResponseEntity<?> getAll(){
    return ResponseEntity.ok(contractService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(contractService.findById(id));
  }

  @PostMapping("")
  public ResponseEntity<?> add(@RequestBody Contract contract){
    return ResponseEntity.ok(contractService.add(contract));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Contract contract){
    return ResponseEntity.ok(contractService.update(id, contract));
  }

  @PutMapping("/{id}/change-status")
  public ResponseEntity<?> changeStatus(@PathVariable("id") long id){
    return ResponseEntity.ok(contractService.changeStatusById(id));
  }
  @PutMapping("/change-status")
  public ResponseEntity<?> changeAllStatusByIds(@RequestBody long[] ids){
    return ResponseEntity.ok(contractService.changeAllStatusByIds(ids));
  }

}
