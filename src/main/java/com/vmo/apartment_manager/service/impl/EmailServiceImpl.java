package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.PersonRepository;
import com.vmo.apartment_manager.service.EmailService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  PersonRepository personRepo;

  @Autowired
  BillRepository billRepository;

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public String sendBill(Bill bill1) {
    try{
      Bill bill = billRepository.findById(1l).get();
      String recipient = personRepo.findRepresentByContractId(bill.getId()).getEmail();
      String content = getContent(bill);
      String subject = "Hóa đơn phí dịch vụ phòng " + bill.getContract().getApartment().getName();

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(recipient);
      mailMessage.setTo(recipient);
      mailMessage.setText(content);
      mailMessage.setSubject(subject);
      javaMailSender.send(mailMessage);
    }catch (Exception e){
      return "Send failed";
    }
    return "Send successfully";
  }



  public String getContent(Bill bill){
    Date date = new Date();
    int i = 1;
    StringBuilder content = new StringBuilder();
    content.append("Hóa đơn dịch vụ tháng " + date.getMonth())
        .append("\n Số phòng: " + bill.getContract().getApartment().getName())
        .append("\n Chủ hộ: " + bill.getContract().getPerson().getFullName())
        .append("\n Số điện thoại: " + bill.getContract().getPerson().getPhone())
        .append("\n Các loại phí cần thanh toán: ")
        .append("\n " + i++ +". Tiền phòng: " + bill.getContract().getPriceApartment());
  List<BillDetail> billDetails = bill.getBillDetailList();
  for(BillDetail billDetail: billDetails){
    content.append("\n" + i++ + " " + billDetail.getServiceFee().getName())
        .append("\t Name service: ")
        .append(billDetail.getServiceFee().getName())
        .append("\t Consume: ")
        .append(billDetail.getServiceFee().getPrice())
        .append("\t Payment: ")
        .append(billDetail.getSubTotal());
  }
  content.append("\nTổng số tiền phải thanh toán: " + bill.getTotal());
    return content.toString();
  }


}
