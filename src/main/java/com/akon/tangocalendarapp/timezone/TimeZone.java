package com.akon.tangocalendarapp.timezone;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "timezones")
public class TimeZone {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotBlank
  @Column(name = "code")
  private String code;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
