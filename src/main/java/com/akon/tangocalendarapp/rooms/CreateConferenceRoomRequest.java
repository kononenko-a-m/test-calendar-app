package com.akon.tangocalendarapp.rooms;

import javax.validation.constraints.NotBlank;

public class CreateConferenceRoomRequest {
  @NotBlank
  private String name;

  @NotBlank
  private String address;

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }
}
