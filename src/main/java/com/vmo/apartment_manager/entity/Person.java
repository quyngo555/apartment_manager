package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Person extends BaseEntity{
  private String fullName;
  private Date dob;
  private String phone;
  private String email;
  private String cin;
  private Boolean gender;
  private String carrer;
  private Integer idParent;

}
