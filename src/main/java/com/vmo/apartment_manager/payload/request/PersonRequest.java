package com.vmo.apartment_manager.payload.request;

import com.vmo.apartment_manager.entity.Person;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonRequest {
  private String fullName;

  private Date dob;

  private String phone;

  @Email
  private String email;

  @Column(unique=true)
  private String cin;

  private Boolean gender;

  private String carrer;

  private Long apartmentId;

  private Boolean status;

  public PersonRequest(Person person, long apartmentId) {
    this.fullName = person.getFullName();
    this.dob = person.getDob();
    this.phone = person.getPhone();
    this.email = person.getEmail();
    this.cin = person.getCin();
    this.gender = person.getGender();
    this.carrer = person.getCarrer();
    this.apartmentId = apartmentId;
    this.status = person.getStatus();
  }
  public PersonRequest(Person person) {
    this.fullName = person.getFullName();
    this.dob = person.getDob();
    this.phone = person.getPhone();
    this.email = person.getEmail();
    this.cin = person.getCin();
    this.gender = person.getGender();
    this.carrer = person.getCarrer();
    this.status = person.getStatus();
  }

}
