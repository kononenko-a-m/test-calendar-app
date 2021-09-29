package com.akon.tangocalendarapp.events;

import com.akon.tangocalendarapp.rooms.ConferenceRoom;
import com.akon.tangocalendarapp.users.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "calendar_events")
public class CalendarEvent {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotNull
  @OneToOne()
  @JoinColumn(name = "owner_id", referencedColumnName = "id")
  private User owner;

  @NotBlank
  @Column(name = "eventName")
  private String eventName;

  @NotNull
  @Column(name = "meeting_agenda")
  private String meetingAgenda;

  @NotNull
  @Column(name = "start")
  private LocalDateTime start;

  @NotNull
  @Column(name = "end")
  private LocalDateTime end;

  @OneToOne
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private ConferenceRoom location;

  @ManyToMany
  @JoinTable(
    name = "calendar_event_participants",
    joinColumns = @JoinColumn(name = "calendar_event_id"),
    inverseJoinColumns = @JoinColumn(name = "participant_id")
  )
  private Set<User> participants;

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getMeetingAgenda() {
    return meetingAgenda;
  }

  public void setMeetingAgenda(String meetingAgenda) {
    this.meetingAgenda = meetingAgenda;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  public ConferenceRoom getLocation() {
    return location;
  }

  public void setLocation(ConferenceRoom location) {
    this.location = location;
  }

  public Set<User> getParticipants() {
    return participants;
  }

  public void setParticipants(Set<User> participants) {
    this.participants = participants;
  }
}
