package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.service.BillService;
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
public class BillController {

  @Autowired
  BillService billService;

  @GetMapping("/bills")
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    return ResponseEntity.ok(billService.getAllBill(pageNo, pageSize, sortBy));
  }

  @GetMapping("/bills/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(billService.findById(id));
  }

  @PostMapping("/bills")
  public ResponseEntity<?> add(@RequestBody Bill bill){
    return ResponseEntity.ok(billService.add(bill));
  }

  @PutMapping("/bills/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Bill bill){
    return ResponseEntity.ok(billService.update(id, bill));
  }
  @GetMapping("/apartments/{id}/getAllBill")
  public ResponseEntity<?> getBillsByApartmentId(@PathVariable("id")long id){
    return ResponseEntity.ok(billService.getBillsByApartmentId(id));
  }

}
