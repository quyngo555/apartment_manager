//package com.vmo.apartment_manager.controller;
//
//import com.vmo.apartment_manager.entity.Bill;
//import com.vmo.apartment_manager.repository.BillRepository;
//import com.vmo.apartment_manager.service.BillService;
//import com.vmo.apartment_manager.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class mailController {
//  @Autowired
//  EmailService emailService;
//  @PostMapping("/api/mail")
//  public ResponseEntity<?> sendMail(Bill bill){
//
//    return ResponseEntity.ok(emailService.sendMail(bill));
//  }
//}
