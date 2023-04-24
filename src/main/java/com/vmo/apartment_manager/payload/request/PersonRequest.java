package com.vmo.apartment_manager.payload.request;

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


}
