package com.akon.tangocalendarapp.users;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotBlank()
  @Column(name = "name")
  private String name;

  @NotBlank()
  @Email
  @Column(name = "email")
  private String email;

  @JsonIgnore()
  @NotNull
  @OneToOne(mappedBy = "user")
  private UserSettings settings;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserSettings getSettings() {
    return settings;
  }

  public void setSettings(UserSettings settings) {
    this.settings = settings;
  }
}
