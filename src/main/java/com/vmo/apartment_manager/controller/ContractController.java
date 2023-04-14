package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Contract;
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
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy) {
    return ResponseEntity.ok(contractService.getAll(pageNo, pageSize, sortBy));
  }

  @GetMapping("/contracts/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id) {
    return ResponseEntity.ok(contractService.findById(id));
  }

  @PostMapping("/contracts")
  public ResponseEntity<?> add(@RequestBody Contract contract) throws Exception {
    return ResponseEntity.ok(contractService.add(contract));
  }

  @PutMapping("/contracts/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Contract contract) {
    return ResponseEntity.ok(contractService.update(id, contract));
  }

  @PutMapping("/contracts/{id}/change-status")
  public ResponseEntity<?> changeStatus(@PathVariable("id") long id) {
    return ResponseEntity.ok(contractService.changeStatusById(id));
  }

  @PutMapping("/contracts/change-status")
  public ResponseEntity<?> changeAllStatusByIds(@RequestBody long[] ids) {
    return ResponseEntity.ok(contractService.changeAllStatusByIds(ids));
  }


  @GetMapping("contracts/search-by-date")
  public ResponseEntity<?> getContractCreatedBetween(@RequestParam Date startDate,
      @RequestParam Date endDate) {
    return ResponseEntity.ok(contractService.findContractByCreatedBetween(startDate, endDate));
  }

}
