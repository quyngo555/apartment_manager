package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.BillDto;
import com.vmo.apartment_manager.dto.BillRequest;
import com.vmo.apartment_manager.dto.ServiceDto;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.ServiceDetailRepository;
import com.vmo.apartment_manager.service.BillService;
import com.vmo.apartment_manager.service.EmailService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {

  @Autowired
  BillRepository billRepo;

  @Autowired
  ServiceDetailRepository serviceDetailRepo;

  @Autowired
  ContractRepository contractRepo;

  @Autowired
  EmailService emailService;

  @Override
  public BillDto add(BillRequest dto) {
    Contract contract = contractRepo.getContractActive(dto.getApartmentId());
    Bill bill = new Bill();
    bill.setStauts(0);
    bill.setContract(contractRepo.findById(contract.getId()).get());
    bill = billRepo.save(bill);
    return new BillDto(bill);
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

    List<ServiceDto> serviceDtos = serviceDetailRepo.findAllByBillId(bill.getId())
        .stream()
        .map(ServiceDto::new)
        .toList();
    BillDto billDto = new BillDto(bill1, serviceDtos);
    return billDto;
  }

  @Override
  public BillDto findById(long id) {
    Bill bill =  billRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + id);
    });
    List<ServiceDto> serviceDtos = serviceDetailRepo.findAllByBillId(bill.getId())
        .stream()
        .map(ServiceDto::new)
        .toList();
    BillDto billDto = new BillDto(bill, serviceDtos);
    return billDto;
  }

  @Override
  public List<BillDto> getAllBill(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    List<Bill> bills =  billRepo.findAll(paging).getContent();
    List<BillDto> billDtos = new ArrayList<>();
    for(Bill bill: bills){
      List<ServiceDto> serviceDtos = serviceDetailRepo.findAllByBillId(bill.getId()).stream()
          .map(ServiceDto::new)
          .toList();
      BillDto dto = new BillDto(bill, serviceDtos);
      billDtos.add(dto);
    }
    return billDtos;
  }

  public Double getTotalFee(Bill bill){
    Double total = (double) 0;
    if(bill.getServiceDetails() == null) return total;
    List<ServiceDetail> serviceDetails = bill.getServiceDetails();
    for(ServiceDetail serviceDetail : serviceDetails){
      total += serviceDetail.getFee();
    }
    total += bill.getContract().getPriceApartment();
    return total;
  }

  @Override
  public List<BillDto> getBillsByApartmentId(long apartmentId) {
    List<Bill> bills = billRepo.getBillsByApartmentId(apartmentId);
    List<BillDto> billDtos = new ArrayList<>();
    for(Bill bill: bills){
      List<ServiceDto> serviceDtos = serviceDetailRepo.findAllByBillId(bill.getId()).stream()
          .map(ServiceDto::new)
          .toList();
      BillDto  dto = new BillDto(bill, serviceDtos);
      billDtos.add(dto);
    }
    return billDtos;
  }
  @Scheduled(cron = "0 15 10 ? * 6L")// Run at 10:15 on the last Friday of the month
  private void sendBill(){
    List<Bill> bills = billRepo.findAll();
    for(Bill bill:bills){
      emailService.sendMail(bill);
    }
  }



}
