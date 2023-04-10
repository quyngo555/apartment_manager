package com.vmo.apartment_manager.dto;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonRequest {
  private String fullName;
  private Date dob;
  private String phone;
  private String email;
  private String cin;
  private Boolean gender;
  private String carrer;
  private Long idParent;
  private Integer status;
  private Long apartmentId;
}
