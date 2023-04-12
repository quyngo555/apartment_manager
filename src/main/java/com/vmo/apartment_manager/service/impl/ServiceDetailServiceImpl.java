package com.vmo.apartment_manager.service.impl;

import static com.vmo.apartment_manager.entity.TypeService.ELECTRICITY;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.dto.ServiceDto;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.ServiceDetail;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ServiceDetailRepository;
import com.vmo.apartment_manager.service.BillService;
import com.vmo.apartment_manager.service.ServiceDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceDetailServiceImpl implements ServiceDetailService {

  @Autowired
  ServiceDetailRepository serviceDetailRepo;

  @Autowired
  BillRepository billRepo;

  @Autowired
  BillService billService;

  @Override
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public ServiceDto add(long billId, ServiceDetail service) {
    if(service.getName().equals(ELECTRICITY)){

    }
    service.setFee(caculateFee(service));
    Bill bill = billRepo.findById(service.getBill().getId()).get();
    service.setBill(bill);
    ServiceDetail serviceDetail = serviceDetailRepo.save(service);
    bill.setTotal(billService.getTotalFee(bill));
    billService.update(bill.getId(), bill);
    return new ServiceDto(serviceDetail);
  }

  @Override
  public ServiceDto update(Long id, ServiceDetail serviceDetail) {
    ServiceDetail serviceDetail1 = serviceDetailRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.SERVICE_NOT_FOUND + id);
    });
    serviceDetail1.setName(serviceDetail.getName());
    serviceDetail1.setNote(serviceDetail.getNote());
    serviceDetail1.setUnit(serviceDetail.getUnit());
    serviceDetail1.setPrice(serviceDetail.getPrice());
    serviceDetail1.setBill(billRepo.findById(serviceDetail.getBill().getId()).orElseThrow(()->{
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    }));
    serviceDetail1 = serviceDetailRepo.save(serviceDetail1);
    return new ServiceDto(serviceDetail1);
  }

  @Override
  public ServiceDto findById(long id) {
    ServiceDetail serviceDetail =  serviceDetailRepo.findById(id).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.SERVICE_NOT_FOUND + id);
    });
    return new ServiceDto(serviceDetail);
  }

  @Override
  public List<ServiceDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
    return serviceDetailRepo.findAll(paging).getContent().stream()
        .map(ServiceDto::new)
        .toList();
  }
  public Double caculateFee(ServiceDetail service){
    return (service.getNextNum() - service.getPreviousNum()) * service.getPrice();
  }

//  private void writeTableHeader(PdfPTable table) {
//    PdfPCell cell = new PdfPCell();
//    cell.setBackgroundColor(Color.BLUE);
//    cell.setPadding(5);
//
//    Font font = FontFactory.getFont(FontFactory.HELVETICA);
//    font.setColor(Color.WHITE);
//
//    cell.setPhrase(new Phrase("STT", font));
//
//    table.addCell(cell);
//
//    cell.setPhrase(new Phrase("TYPE SERVICE", font));
//    table.addCell(cell);
//
//    cell.setPhrase(new Phrase("PREVIOUS NUM", font));
//    table.addCell(cell);
//
//    cell.setPhrase(new Phrase("NEXT NUM", font));
//    table.addCell(cell);
//
//    cell.setPhrase(new Phrase("PAYMENT", font));
//    table.addCell(cell);
//  }
//  private void writeTableData(PdfPTable table) {
//    Bill bill = billRepo.findById(id);
//    List<ServiceDetail> serviceDetails =
//    for (User user : listUsers) {
//      table.addCell(String.valueOf(user.getId()));
//      table.addCell(user.getEmail());
//      table.addCell(user.getFullName());
//      table.addCell(user.getRoles().toString());
//      table.addCell(String.valueOf(user.isEnabled()));
//    }
//  }
}
