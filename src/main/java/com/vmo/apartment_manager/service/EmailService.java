package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.BillDetail;

public interface EmailService {
  String sendBill(Bill bill);
  void sendContractWarning();
}
