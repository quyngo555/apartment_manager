package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.ServiceDetailRepository;
import com.vmo.apartment_manager.service.BillService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {

  @Autowired
  BillRepository billRepo;

  @Autowired
  ServiceDetailRepository serviceDetailRepo;

  @Autowired
  ContractRepository contractRepo;

  @Override
  public Bill add(Bill bill) {
    bill.setTotal(getTotalFee(bill));
    bill = billRepo.save(bill);
    return bill;
  }

  @Override
  public Bill update(Long id, Bill bill) {
    Bill bill1 = billRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    });
    bill1.setTotal(bill.getTotal());
    bill1.setStauts(bill.getStauts());
    bill1.setDateOfPayment(bill.getDateOfPayment());
    bill1.setContract(bill.getContract());
    return bill1;
  }

  @Override
  public Bill findById(long id) {
    return billRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
  }

  @Override
  public List<Bill> getAllBill() {
    return billRepo.findAll();
  }

  public Double getTotalFee(Bill bill){
    double total = 0;
    List<ServiceDetail> serviceDetails = bill.getServiceDetails();
    for(ServiceDetail serviceDetail : serviceDetails){
      total += serviceDetail.getFee();
    }
    total += bill.getContract().getPriceApartment();
    return total;
  }

}
