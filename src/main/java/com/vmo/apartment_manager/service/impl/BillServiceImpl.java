package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.dto.BillDto;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.BillService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {

  @Autowired
  BillRepository billRepo;
  @Autowired
  BillDetailRepository billDetailRepo;
  @Autowired
  ContractRepository contractRepo;
  @Autowired
  ServiceFeeRepository serviceFeeRepo;

  @Override
  public Bill add(Bill bill) {
    bill.setStauts(0);
    return billRepo.save(bill);
  }

  @Override
  public Bill update(long id, Bill bill) {
    Bill bill1 = billRepo.findById(bill.getId()).get();
    List<BillDetail> billDetailList =bill1.getBillDetailList();
    Double total = 0.0;
    for(BillDetail billDetail: billDetailList){
      total += billDetail.getSubTotal();
    }
    bill1.setPaidDate(bill.getPaidDate());
    bill1.setStauts(bill.getStauts());
    bill1.setTotal(total);
    return billRepo.save(bill1);
  }

  @Override
  public Bill findById(long id) {
    Bill bill = billRepo.findById(id).get();
    return billRepo.findById(id).get();
  }

  @Override
  public List<BillDto> findAll() {
     return billRepo.findAll().stream()
        .map(BillDto:: new)
        .toList();

  }
  @Scheduled(cron = "0 15 10 ? * 6L")
  private void autoGenerateBills(){
    LocalDate termPayment =  LocalDate.now().plusDays(7);
    List<Contract> contracts = contractRepo.findContractActive();
    for(Contract contract: contracts){
      Bill bill = new Bill();
      bill.setContract(contract);
      bill.setTermPayment(Date.valueOf(termPayment));
      bill = billRepo.save(bill);
      for(long i = 1; i <= 2; i++){
        BillDetail billDetail = new BillDetail();
        billDetail.setServiceFee(serviceFeeRepo.findById(i).get());
        billDetail.setBill(bill);
        billDetailRepo.save(billDetail);
      }
    }
  }
}
