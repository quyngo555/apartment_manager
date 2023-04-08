package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.BillDto;
import com.vmo.apartment_manager.dto.ServiceDto;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.ServiceDetailRepository;
import com.vmo.apartment_manager.service.BillService;
import java.util.ArrayList;
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
    bill.setStauts(0);
    bill = billRepo.save(bill);
    return bill;
  }

  @Override
  public BillDto update(Long id, Bill bill) {
    Bill bill1 = billRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    });
    bill1.setTotal( getTotalFee(bill1));
    bill1.setStauts(bill.getStauts());
    bill1.setDateOfPayment(bill.getDateOfPayment());
    bill1 = billRepo.save(bill1);

    BillDto billDto = new BillDto();
    billDto.setId(bill.getId());
    billDto.setTotal(bill1.getTotal());
    billDto.setStauts(bill.getStauts());
    billDto.setFeeRentApartment(bill.getContract().getPriceApartment());
    billDto.setServiceDetailList(serviceDetailRepo.findAllByBillId(bill.getId()).stream()
        .map(ServiceDto::new)
        .toList());
    return billDto;
  }

  @Override
  public BillDto findById(long id) {
    Bill bill =  billRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    BillDto dto = new BillDto();
    dto.setId(bill.getId());
    dto.setTotal(bill.getTotal());
    dto.setStauts(bill.getStauts());
    dto.setFeeRentApartment(bill.getContract().getPriceApartment());
    dto.setServiceDetailList(serviceDetailRepo.findAllByBillId(bill.getId()).stream()
        .map(ServiceDto::new)
        .toList());
    return dto;
  }

  @Override
  public List<BillDto> getAllBill() {
    List<Bill> bills =  billRepo.findAll();
    List<BillDto> billDtos = new ArrayList<>();
    for(Bill bill: bills){
      BillDto dto = new BillDto();
      dto.setId(bill.getId());
      dto.setTotal(bill.getTotal());
      dto.setStauts(bill.getStauts());
      dto.setFeeRentApartment(bill.getContract().getPriceApartment());
      dto.setServiceDetailList(serviceDetailRepo.findAllByBillId(bill.getId()).stream()
          .map(ServiceDto::new)
          .toList());
      billDtos.add(dto);
    }
    return billDtos;
  }

  public Double getTotalFee(Bill bill){
    Double total = (double) 0;
    System.out.println(bill.getId());
    if(serviceDetailRepo.findAllByBillId(bill.getId()) == null) return total;
    List<ServiceDetail> serviceDetails = serviceDetailRepo.findAllByBillId(bill.getId());
    for(ServiceDetail serviceDetail : serviceDetails){
      total += serviceDetail.getFee();
    }
    total += bill.getContract().getPriceApartment();
    return total;
  }

}
