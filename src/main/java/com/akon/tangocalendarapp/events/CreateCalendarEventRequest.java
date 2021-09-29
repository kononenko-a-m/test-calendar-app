package com.akon.tangocalendarapp.events;

import com.akon.tangocalendarapp.common.email.EmailList;

import javax.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

public class CreateCalendarEventRequest {
  public static final int MAX_MEETING_LENGTH_IN_HOURS = 8;

  @NotBlank
  private String eventName;

  @NotBlank
  private String meetingAgenda;

  @Future
  @NotNull
  private LocalDateTime start;

  @Future
  @NotNull
  private LocalDateTime end;

  @Size(min = 1)
  @EmailList
  private Set<String> participants;

  private UUID location;

  public String getEventName() {
    return eventName;
  }

  public String getMeetingAgenda() {
    return meetingAgenda;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public Set<String> getParticipants() {
    return participants;
  }

  public UUID getLocation() {
    return location;
  }

  // In general this should be done by implementing custom Class level validator,
  // but I'm out of time for this task, so gonna use isValid
  @AssertTrue(message="Event cannot be longer than " + MAX_MEETING_LENGTH_IN_HOURS + " hours, and start should be after end")
  public boolean isValid() {
    return end.isAfter(start) && end.minus(MAX_MEETING_LENGTH_IN_HOURS, ChronoUnit.HOURS).isBefore(start);
  }
}
