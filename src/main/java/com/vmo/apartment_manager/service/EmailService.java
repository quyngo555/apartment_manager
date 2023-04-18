package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.BillDetail;

public interface EmailService {
  String sendMail(BillDetail billDetail);
}
