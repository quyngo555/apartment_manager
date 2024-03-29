package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.Person;
import com.vmo.apartment_manager.payload.request.ContractRequest;
import com.vmo.apartment_manager.service.ContractService;
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
public class ContractController {

  @Autowired
  ContractService contractService;

  @GetMapping("/contracts")
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy) {
    return ResponseEntity.ok(contractService.getAllContractActive(pageNo, pageSize, sortBy));
  }

  @GetMapping("/contracts/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id) {
    return ResponseEntity.ok(contractService.findById(id));
  }

  @PostMapping("/contracts")
  public ResponseEntity<?> add(@RequestBody ContractRequest contract) throws Exception {
    return ResponseEntity.ok(contractService.add(contract));
  }

  @PutMapping("/contracts/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Contract contract) {
    return ResponseEntity.ok(contractService.update(id, contract));
  }

  @PutMapping("/contracts/{id}/change-represent")
  public ResponseEntity<?> changeRepresent(@PathVariable("id") long id,
      @RequestParam long representIdNew) {
    return ResponseEntity.ok(contractService.changeRepersent(id, representIdNew));
  }

  @PutMapping("/contracts/change-status")
  public ResponseEntity<?> changeAllStatusByIds(@RequestBody long[] ids) {
    return ResponseEntity.ok(contractService.changeAllStatusByIds(ids));
  }


  @GetMapping("contracts/search-by-code")
  public ResponseEntity<?> getContractCreatedBetween(@RequestParam("contractCode") String contractCode) {
    return ResponseEntity.ok(contractService.findContractByCode(contractCode));
  }

  @GetMapping("/apartments/{id}/contract")
  public ResponseEntity<?> getContractActiveByApartmentId(@PathVariable("id") long id) {
    return ResponseEntity.ok(contractService.findContractByApartmentId(id));
  }


}
