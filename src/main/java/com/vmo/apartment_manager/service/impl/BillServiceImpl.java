package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.payload.request.BillRequest;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.BillDetailService;
import com.vmo.apartment_manager.service.BillService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BillServiceImpl implements BillService {

  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "Apartment_code", "Electric_num", "Electric_fee", "Water_num", "Water_fee", "Total"};
  static String SHEET = "Bills";

  @Autowired
  BillRepository billRepo;
  @Autowired
  BillDetailRepository billDetailRepo;
  @Autowired
  ContractRepository contractRepo;
  @Autowired
  ServiceFeeRepository serviceFeeRepo;

  @Autowired
  BillDetailService billDetailService;

  @Override
  public Bill add(BillRequest bill) {
    Bill bill1 = new Bill();
    bill1.setTermPayment(bill.getTermPayment());
    Contract contract = contractRepo.findContractByApartmentId(bill.getApartmentId()).get();
    billRepo.save(bill1);
    bill1.setBillDetailList(bill.getBillDetailList());
    bill1.setContract(contract);
    bill1.setNote(bill.getNote());
    List<BillDetail> billDetails = new ArrayList<>();
    List<Long> serviceFeeIds = new ArrayList<>();
    for(BillDetail billDetail: bill.getBillDetailList()){
      if(serviceFeeIds.contains(billDetail.getServiceFee().getId()))
        throw new NotFoundException(ConstantError.SERVICE_EXISTS + billDetail.getServiceFee().getId());
      serviceFeeIds.add(billDetail.getServiceFee().getId());
      ServiceFee serviceFee = serviceFeeRepo.findById(billDetail.getServiceFee().getId()).get();
      billDetail.setSubTotal(serviceFee.getPrice()*billDetail.getConsume());
      billDetail.setConsume(billDetail.getConsume());
      billDetails.add(billDetail);
    }
    billDetailRepo.saveAll(billDetails);

    return bill1;
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
  public List<Bill> findAll() {
     return billRepo.findAll();

  }

  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public void importExcel(InputStream is) {
    try {
      LocalDate currentDate =  LocalDate.now();
      int  month = currentDate.getMonthValue();
      int year = currentDate.getYear();
      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet(SHEET);
      Iterator<Row> rows = sheet.iterator();

      List<Bill> bills = new ArrayList<Bill>();

      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();
        Bill bill = new Bill();
        List<BillDetail> billDetails = new ArrayList<>();

        bill.setTermPayment(Date.valueOf(year + "-" + month + "-15"));
        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
            case 0:
              String apartmentCode = currentCell.getStringCellValue();
              Contract contract = contractRepo.findContractByApartmentCode(apartmentCode);
              bill.setContract(contract);
              bill = billRepo.save(bill);
              break;

            case 1:
              Double electricNum = currentCell.getNumericCellValue();
              ServiceFee serviceFee = serviceFeeRepo.findById(1l).get();
              BillDetail billDetail = new BillDetail();
              billDetail.setConsume(electricNum);
              billDetail.setBill(bill);
              billDetail.setServiceFee(serviceFee);
              billDetail.setSubTotal(billDetail.getConsume()*serviceFee.getPrice());
              billDetail = billDetailRepo.save(billDetail);
              billDetails.add(billDetail);
              break;

            case 2:
              Double waterNum = currentCell.getNumericCellValue();
              ServiceFee serviceFee1 = serviceFeeRepo.findById(2l).get();
              BillDetail billDetail1 = new BillDetail();
              billDetail1.setConsume(waterNum);
              billDetail1.setBill(bill);
              billDetail1.setServiceFee(serviceFee1);
              billDetail1.setSubTotal(billDetail1.getConsume()*serviceFee1.getPrice());
              billDetail1 = billDetailRepo.save(billDetail1);
              billDetails.add(billDetail1);
              break;


            default:
              break;
          }
          cellIdx++;
        }
        double total = 0;
        for(BillDetail item: billDetails){
          total += item.getSubTotal();
        }
        bill.setTotal(total);
        billRepo.save(bill);
      }

      workbook.close();

    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }

  public boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public ByteArrayInputStream exportExcel(Date startDate, Date endDate) {

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
      Sheet sheet = workbook.createSheet(SHEET);

      // Header
      Row headerRow = sheet.createRow(0);

      for (int col = 0; col < HEADERs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(HEADERs[col]);
      }

      List<Bill> bills = billRepo.findByCreatedDateBetween(startDate, endDate);
      int rowIdx = 1;
      for (Bill bill : bills) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(bill.getContract().getApartment().getCode());
        for(BillDetail billDetail: bill.getBillDetailList()){
          if(billDetail.getServiceFee().getName() .equals("ELECTRICITY")){
            row.createCell(1).setCellValue(billDetail.getConsume());
            row.createCell(1).setCellValue(billDetail.getServiceFee().getPrice());
          }
          if(billDetail.getServiceFee().getName() .equals("WATER")){
            row.createCell(1).setCellValue(billDetail.getConsume());
            row.createCell(1).setCellValue(billDetail.getServiceFee().getPrice());
          }
        }
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
    }
  }
}
