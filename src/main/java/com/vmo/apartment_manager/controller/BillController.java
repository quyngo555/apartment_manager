package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.payload.request.BillRequest;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.service.BillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Authorization")
public class BillController {

  @Autowired
  BillService billService;

  @PostMapping("/bills")
  public ResponseEntity<?> add(@RequestBody BillRequest bill) {
    Bill bill1 = billService.add(bill);
    bill1 = billService.update(bill1.getId(), bill1);
    return ResponseEntity.ok(bill1);
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

  @PostMapping("/bills/upload")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
    if(billService.hasExcelFormat(file)){
      try {
        billService.importExcel(file.getInputStream());
      }catch (IOException e){
        throw new RuntimeException("fail to store excel data: ");
      }
    }
    return ResponseEntity.noContent().build();
  }
}
