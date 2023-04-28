package com.vmo.apartment_manager.payload.response;

import com.vmo.apartment_manager.entity.Apartment;
import com.vmo.apartment_manager.entity.Person;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponse {
  private Long id;
  private String fullName;
  private Date dob;
  private String phone;
  private String email;
  private String cin;
  private Boolean gender;
  private String carrer;
  private Apartment apartment;
  private Boolean status;

  public PersonResponse(Person person, Apartment apartment) {
    this.id = person.getId();
    this.fullName = person.getFullName();
    this.dob = person.getDob();
    this.phone = person.getPhone();
    this.email = person.getEmail();
    this.cin = person.getCin();
    this.status = person.getStatus();
    this.gender = person.getGender();
    this.carrer = person.getCarrer();
    this.apartment = apartment;
  }

  public PersonResponse(Person person) {
    this.id = person.getId();
    this.fullName = person.getFullName();
    this.dob = person.getDob();
    this.phone = person.getPhone();
    this.email = person.getEmail();
    this.cin = person.getCin();
    this.status = person.getStatus();
    this.gender = person.getGender();
    this.carrer = person.getCarrer();
  }

}
