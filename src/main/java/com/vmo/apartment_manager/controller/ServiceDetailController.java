package com.vmo.apartment_manager.controller;

import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.service.ServiceDetailService;
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
@RequestMapping("/api/services-detail")
public class ServiceDetailController {

  @Autowired
  ServiceDetailService serviceDetailService;

  @GetMapping("")
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    return ResponseEntity.ok(serviceDetailService.getAll(pageNo, pageSize, sortBy));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") long id){
    return ResponseEntity.ok(serviceDetailService.findById(id));
  }

  @PostMapping("")
  public ResponseEntity<?> add(@RequestBody ServiceDetail serviceDetail){
    return ResponseEntity.ok(serviceDetailService.add(serviceDetail));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody ServiceDetail serviceDetail){
    return ResponseEntity.ok(serviceDetailService.update(id, serviceDetail));
  }
}
