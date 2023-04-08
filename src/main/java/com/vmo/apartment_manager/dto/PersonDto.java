package com.vmo.apartment_manager.dto;

import com.vmo.apartment_manager.entity.Person;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto {
  private Long id;
  private String fullName;
  private Date dob;
  private String phone;
  private String email;
  private String cin;
  private Boolean gender;
  private String carrer;
  private Long idParent;
  private String apartmentName;
  private Integer status;

  public PersonDto(Person person, String apartmentName) {
    this.id = person.getId();
    this.fullName = person.getFullName();
    this.dob = person.getDob();
    this.phone = person.getPhone();
    this.email = person.getEmail();
    this.cin = person.getCin();
    this.gender = person.getGender();
    this.carrer = person.getCarrer();
    this.idParent = person.getIdParent();
    this.apartmentName = apartmentName;
    this.status = person.getStatus();
  }
}
