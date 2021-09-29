package com.akon.tangocalendarapp.events;

import com.akon.tangocalendarapp.rooms.ConferenceRoomService;
import com.akon.tangocalendarapp.users.UnableToResolveUsersByEmailsException;
import com.akon.tangocalendarapp.users.User;
import com.akon.tangocalendarapp.users.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class CalendarEventService {

  private final ConferenceRoomService conferenceRoomService;
  private final CalendarEventRepository calendarEventRepository;
  private final UserService userService;

  public CalendarEventService(ConferenceRoomService conferenceRoomService, CalendarEventRepository calendarEventRepository, UserService userService) {
    this.conferenceRoomService = conferenceRoomService;
    this.calendarEventRepository = calendarEventRepository;
    this.userService = userService;
  }

  @Transactional
  public CalendarEvent createCalendarEvent(User currentUser, CreateCalendarEventRequest createCalendarEventRequest) throws ConflictingEventsException, UnableToResolveUsersByEmailsException {
    UUID locationRoomId = createCalendarEventRequest.getLocation();
    var start = createCalendarEventRequest.getStart();
    var end = createCalendarEventRequest.getEnd();

    var conflictingEvents = calendarEventRepository.findCalendarEventRangeWithLock(locationRoomId, start, end);
    var participantEmails = createCalendarEventRequest.getParticipants();

    var participants = userService.resolveUsersByEmails(participantEmails);

    // let's ensure that owner is on the list of participants
    participants.add(currentUser);

    if (!conflictingEvents.isEmpty()) {
      throw new ConflictingEventsException(conflictingEvents);
    }

    var calendarEvent = new CalendarEvent();

    calendarEvent.setOwner(currentUser);
    calendarEvent.setEventName(createCalendarEventRequest.getEventName());
    calendarEvent.setMeetingAgenda(createCalendarEventRequest.getMeetingAgenda());
    calendarEvent.setStart(start);
    calendarEvent.setEnd(end);
    calendarEvent.setParticipants(participants);

    if (locationRoomId != null) {
      calendarEvent.setLocation(
        // we can get directly room as it's locked in current transaction
        conferenceRoomService.getRoomById(locationRoomId).get()
      );
    }

    calendarEventRepository.save(calendarEvent);

    return calendarEvent;
  }

  public Set<CalendarEvent> getAllUserCalendarEvents(User user) {
    return calendarEventRepository.findVisibleForUserCalendarEvents(user);
  }

  public Set<CalendarEvent> getAllUserCalendarEventsByQuery(User user, String query) {
    return calendarEventRepository.findByUserIdAndQuery(user.getId(), query);
  }

  public Set<CalendarEvent> getAllUserCalendarEventsByLocationId(User user, UUID locationId) {
    return calendarEventRepository.findByUserAndLocationId(user, locationId);
  }

  public Set<CalendarEvent> getAllUserCalendarEventsByDay(User user, LocalDateTime start, LocalDateTime end) {
    return calendarEventRepository.findByRange(
      user,
      start,
      end
    );
  }
}
