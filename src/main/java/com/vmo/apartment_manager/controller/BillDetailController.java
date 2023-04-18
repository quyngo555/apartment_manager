package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.service.BillDetailService;
import com.vmo.apartment_manager.service.BillService;
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
@RequestMapping("/api")
@SecurityRequirement(name = "Authorization")
public class BillDetailController {

  @Autowired
  BillDetailService billDetailService;
  @Autowired
  BillService billService;

  @PostMapping("/bill-details")
  public ResponseEntity<?> add(@RequestBody BillDetail billDetail) {
    BillDetail billDetail1 = billDetailService.add(billDetail);
    billService.update(billDetail1.getBill().getId(), billDetail1.getBill());
    return ResponseEntity.ok(billDetail1);
  }

  @PutMapping("/bill-details/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody BillDetail billDetail) {
    BillDetail billDetail1 = billDetailService.add(billDetail);
    billService.update(billDetail1.getBill().getId(), billDetail1.getBill());
    return ResponseEntity.ok(billDetail1);
  }

}
