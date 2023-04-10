package com.vmo.apartment_manager.service;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Bill;
import com.vmo.apartment_manager.entity.Person;
import java.util.List;

public interface EmailService {
  String sendMail(Bill bill);
}
