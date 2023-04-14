package com.vmo.apartment_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
public class Contract extends BaseEntity{

  private double priceApartment;

  private Date startDate;

  private Date endDate;

  private Long idUser;

  private String code;

  private Integer status;

  @ManyToOne
  @JoinColumn(name = "apartment_id")
  private Apartment apartment;

  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;
}
