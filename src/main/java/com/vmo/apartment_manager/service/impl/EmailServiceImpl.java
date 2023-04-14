//package com.vmo.apartment_manager.service.impl;
//
//import com.vmo.apartment_manager.entity.Bill;
//import com.vmo.apartment_manager.repository.BillRepository;
//import com.vmo.apartment_manager.repository.PersonRepository;
//import com.vmo.apartment_manager.service.EmailService;
//import java.util.Date;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//  @Autowired
//  PersonRepository personRepo;
//
//  @Autowired
//  BillRepository billRepository;
//
//  @Autowired
//  private JavaMailSender javaMailSender;
//
//  @Override
//  public String sendMail(Bill bill1) {
//    try{
//      Bill bill = billRepository.findById(1l).get();
//      String recipient = personRepo.getEmailPresentedByBillId(bill.getId());
//      String content = getContent(bill);
//      String subject = "Hóa đơn phí dịch vụ phòng " + bill.getContract().getApartment().getName();
//
//      SimpleMailMessage mailMessage = new SimpleMailMessage();
//      mailMessage.setFrom(recipient);
//      mailMessage.setTo(recipient);
//      mailMessage.setText(content);
//      mailMessage.setSubject(subject);
//      javaMailSender.send(mailMessage);
//    }catch (Exception e){
//      return "Send failed";
//    }
//    return "Send successfully";
//  }
//  public String getContent(Bill bill){
//    Date date = new Date();
//    int i = 1;
//    StringBuilder content = new StringBuilder();
//    content.append("Hóa đơn dịch vụ tháng " + date.getMonth())
//        .append("\n Số phòng: " + bill.getContract().getApartment().getName())
//        .append("\n Chủ hộ: " + bill.getContract().getPerson().getFullName())
//        .append("\n Số điện thoại: " + bill.getContract().getPerson().getPhone())
//        .append("\n Các loại phí cần thanh toán: ")
//        .append("\n " + i++ +". Tiền phòng: " + bill.getContract().getPriceApartment());
//  List<ServiceDetail> serviceDetails = bill.getServiceDetails();
//  for(ServiceDetail serviceDetail: serviceDetails){
//    content.append("\n" + i++ + " " + serviceDetail.getName())
//        .append("\t Previous Num: ")
//        .append(serviceDetail.getPreviousNum())
//        .append("\t Next Num: ")
//        .append(serviceDetail.getNextNum())
//        .append("\t Payment: ")
//        .append(serviceDetail.getFee());
//  }
//  content.append("\nTổng số tiền phải thanh toán: " + bill.getTotal());
//    return content.toString();
//  }
//
//
//}
