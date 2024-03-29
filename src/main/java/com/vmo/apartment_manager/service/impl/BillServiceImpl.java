package com.vmo.apartment_manager.service.impl;

import com.vmo.apartment_manager.constant.ConstantError;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;
import com.vmo.apartment_manager.entity.Contract;
import com.vmo.apartment_manager.entity.ServiceFee;
import com.vmo.apartment_manager.entity.TypeService;
import com.vmo.apartment_manager.exception.BadRequestException;
import com.vmo.apartment_manager.exception.NotFoundException;
import com.vmo.apartment_manager.payload.request.BillRequest;
import com.vmo.apartment_manager.payload.response.BillResponse;
import com.vmo.apartment_manager.repository.BillDetailRepository;
import com.vmo.apartment_manager.repository.BillRepository;
import com.vmo.apartment_manager.repository.ContractRepository;
import com.vmo.apartment_manager.repository.ServiceFeeRepository;
import com.vmo.apartment_manager.service.BillDetailService;
import com.vmo.apartment_manager.service.BillService;
import com.vmo.apartment_manager.service.EmailService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BillServiceImpl implements BillService {

  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = {"Apartment_code", "Electric_num", "Electric_fee", "Water_num",
      "Water_fee", "Total"};
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
  EmailService emailService;

  @Autowired
  BillDetailService billDetailService;

  // add bill
  @Override
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public Bill add(BillRequest bill) {
    Bill bill1 = new Bill();
    Contract contract = contractRepo.findContractByApartmentId(bill.getApartmentId()).orElseThrow(() ->{
      throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + "in apartmentId: " + bill.getApartmentId());
    });
    bill1.setContract(contract);
    bill1.setNote(bill.getNote());
    billRepo.save(bill1);
    bill1.setBillDetailList(bill.getBillDetailList());
    List<BillDetail> billDetails = new ArrayList<>();
    List<Long> serviceFeeIds = new ArrayList<>();
    double total = 0;
    for (BillDetail billDetail : bill.getBillDetailList()) {
      if (serviceFeeIds.contains(billDetail.getServiceFee().getId())) {
        throw new NotFoundException(
            ConstantError.SERVICE_EXISTS + billDetail.getServiceFee().getId());
      }
      serviceFeeIds.add(billDetail.getServiceFee().getId());
      ServiceFee serviceFee = serviceFeeRepo.findById(billDetail.getServiceFee().getId()).orElseThrow(() -> {
        throw new NotFoundException(ConstantError.SERVICE_NOT_EXISTS + billDetail.getServiceFee().getId());
      });
      billDetail.setSubTotal(serviceFee.getPrice() * billDetail.getConsume());
      billDetail.setConsume(billDetail.getConsume());
      billDetail.setBill(bill1);
      total += billDetail.getSubTotal();
      bill1.setTotal(total);

      billDetails.add(billDetail);
    }
    billDetailRepo.saveAll(billDetails);
    billRepo.save(bill1);

    return bill1;
  }

  // update bill
  @Override
  public BillResponse update(long id, Bill bill) {
    Bill bill1 = billRepo.findById(id).orElseThrow(() -> {
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    });
    List<BillDetail> billDetailList = bill1.getBillDetailList();
    Double total = 0.0;
    for (BillDetail billDetail : billDetailList) {
      total += billDetail.getSubTotal();
    }
    bill.setId(id);
    bill.setTotal(total);
    return new BillResponse(billRepo.save(bill));
  }

  // get bill by id
  @Override
  public BillResponse findById(long id) {
    Bill bill = billRepo.findById(id).orElseThrow(()-> {
      throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
    });
    return new BillResponse(bill);
  }

  // get all bill
  @Override
  public Page<BillResponse> findAll(Integer pageNo,Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).descending());
    return billRepo.findAll(paging).map(BillResponse::new);

  }

  // import bill by excel
  @Transactional(rollbackFor = {Exception.class, Throwable.class})
  public String importExcel(InputStream is) {
    try {
      LocalDate currentDate = LocalDate.now();
      int month = currentDate.getMonthValue();
      int year = currentDate.getYear();
      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet(SHEET);
      Iterator<Row> rows = sheet.iterator();

      List<Bill> bills = new ArrayList<Bill>();
      ServiceFee serviceFeeEletric = serviceFeeRepo.findServiceFeeByName(TypeService.ELECTRICITY);
      ServiceFee serviceFeeWater = serviceFeeRepo.findServiceFeeByName(TypeService.WATER);
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
              if(apartmentCode == null)
                throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + apartmentCode);
              Contract contract = contractRepo.findContractByApartmentCode(apartmentCode).orElseThrow(()->{
                throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND);
              });
              if(contract == null){
                throw new NotFoundException(ConstantError.CONTRACT_NOT_EXISTS_IN_APARTMENT + apartmentCode);
              }
              if (contract != null) {
                bill.setContract(contract);
                bill.setStauts(false);
                bill = billRepo.save(bill);
              } else {
                throw new NotFoundException(ConstantError.CONTRACT_NOT_FOUND + "in apartment: " + apartmentCode);
              }

              break;

            case 1:
              Double electricNum = currentCell.getNumericCellValue();
              if(electricNum == null){
                throw new NotFoundException(ConstantError.FILE_IMPORT_ERROR + cellIdx);
              }
              BillDetail billDetail = new BillDetail();
              billDetail.setConsume(electricNum);
              if (bill != null) {
                billDetail.setBill(bill);
                billDetail.setServiceFee(serviceFeeEletric);
                billDetail.setSubTotal(billDetail.getConsume() * serviceFeeEletric.getPrice());
                billDetail = billDetailRepo.save(billDetail);
                billDetails.add(billDetail);
              }

              break;

            case 2:
              Double waterNum = currentCell.getNumericCellValue();
              if(waterNum == null){
                throw new NotFoundException(ConstantError.FILE_IMPORT_ERROR + cellIdx);
              }

              BillDetail billDetailWater = new BillDetail();
              billDetailWater.setConsume(waterNum);
              if (bill != null) {
                billDetailWater.setBill(bill);
                billDetailWater.setServiceFee(serviceFeeWater);
                billDetailWater.setSubTotal(billDetailWater.getConsume() * serviceFeeWater.getPrice());
                billDetailWater = billDetailRepo.save(billDetailWater);
                billDetails.add(billDetailWater);
              }
              break;

            default:
              break;
          }
          cellIdx++;
        }
        double total = 0;
        for (BillDetail item : billDetails) {
          total += item.getSubTotal();
        }
        bill.setTotal(total);
        billRepo.save(bill);
      }

      workbook.close();
      return "Import data succedd.";

    } catch (IOException e) {
      throw new BadRequestException("fail to parse Excel file: " + e.getMessage());
    }
  }

  // check format file
  public boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  // export bills to excel
  @Override
  public ByteArrayInputStream exportExcel(Date startDate, Date endDate) {

    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();) {
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
        for (BillDetail billDetail : bill.getBillDetailList()) {
          if (billDetail.getServiceFee().getName() == TypeService.ELECTRICITY) {
            row.createCell(1).setCellValue(billDetail.getConsume());
            row.createCell(2).setCellValue(billDetail.getServiceFee().getPrice());
          }
          if (billDetail.getServiceFee().getName() == TypeService.WATER) {
            row.createCell(3).setCellValue(billDetail.getConsume());
            row.createCell(4).setCellValue(billDetail.getServiceFee().getPrice());
          }
        }
        row.createCell(5).setCellValue(bill.getTotal());
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new BadRequestException("fail to import data to Excel file: " + e.getMessage());
    }
  }

  // get bill created between dates
  @Override
  public Page<BillResponse> findBillByCreatedBetween(Date startDate, Date endDate, String apartmentCode, Integer pageNo, Integer pageSize, String sortBy) {
    Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
    if(apartmentCode.equals("all")){
      return billRepo.findByCreatedDateBetweenDatesWithPagination(startDate, endDate, paging)
              .map(BillResponse::new);

    } else{
      return billRepo.findByCreatedDateBetweenDatesWithPagination(startDate, endDate, apartmentCode, paging)
              .map(BillResponse::new);
    }
  }

  // send bill
  @Scheduled(cron = "0 15 10 ? * 6L") // Run at 10:15 on the last Friday of the month
  public void sendBill() {
    List<Bill> bills = billRepo.findBillUnPaid();
    for (Bill bill : bills) {
      emailService.sendBill(bill);
    }
  }





}
