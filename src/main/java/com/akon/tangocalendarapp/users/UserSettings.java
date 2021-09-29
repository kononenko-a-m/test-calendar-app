package com.akon.tangocalendarapp.users;

import com.akon.tangocalendarapp.timezone.TimeZone;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_settings")
public class UserSettings {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @OneToOne()
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToOne()
  @JoinColumn(name = "timezone_id", referencedColumnName = "id")
  private TimeZone timeZone;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }
}
