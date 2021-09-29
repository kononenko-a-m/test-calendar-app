package com.akon.tangocalendarapp.rooms;

import com.akon.tangocalendarapp.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "conference_rooms")
public class ConferenceRoom {

  @NotNull
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotNull
  @OneToOne
  @JoinColumn(name = "manager_id", referencedColumnName = "id")
  private User manager;

  @NotBlank
  @Column(name = "name")
  private String name;

  @NotBlank
  @Column(name = "address")
  private String address;

  public UUID getId() {
    return id;
  }

  public User getManager() {
    return manager;
  }

  public void setManager(User manager) {
    this.manager = manager;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
