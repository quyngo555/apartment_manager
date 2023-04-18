package com.vmo.apartment_manager.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
