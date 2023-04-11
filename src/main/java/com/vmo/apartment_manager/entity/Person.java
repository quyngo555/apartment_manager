package com.vmo.apartment_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
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

  @Email
  private String email;

  @Column(unique=true)
  private String cin;

  private Boolean gender;

  private String carrer;

  private Long idParent;

  private Integer status;

}
