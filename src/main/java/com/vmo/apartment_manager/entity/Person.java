package com.vmo.apartment_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Person extends BaseEntity{

  @NotNull(message = "Name is mandatory")
  private String fullName;

  private Date dob;

  private String phone;

  @Email(message = "Email must be in the correct format")
  private String email;

  @Column(unique=true)
  @Size(min = 3, max = 15, message = "The length of password is not valid")
  private String cin;

  @NotNull(message = "Gender is mandatory")
  private Boolean gender;

  private String carrer;

  private Long contractId;

  private Boolean status;

}
