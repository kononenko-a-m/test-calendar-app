package com.akon.tangocalendarapp.events;

import com.akon.tangocalendarapp.timezone.LocalDateConverter;
import com.akon.tangocalendarapp.users.UnableToResolveUsersByEmailsException;
import com.akon.tangocalendarapp.users.UserDetailsImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/calendar-events")
public class CalendarEventController {

  private final LocalDateConverter localDateConverter;
  private final CalendarEventService calendarEventService;

  public CalendarEventController(LocalDateConverter localDateConverter, CalendarEventService calendarEventService) {
    this.localDateConverter = localDateConverter;
    this.calendarEventService = calendarEventService;
  }

  @GetMapping(params = "query")
  public Set<CalendarEvent> getAllCalendarEventsByQuery(
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @RequestParam String query
  ) {
    return calendarEventService.getAllUserCalendarEventsByQuery(userDetails.getUser(), query);
  }

  @GetMapping(params = "location_id")
  public Set<CalendarEvent> getAllCalendarEventsByLocation(
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @RequestParam UUID locationId
  ) {
    return calendarEventService.getAllUserCalendarEventsByLocationId(userDetails.getUser(), locationId);
  }

  @GetMapping(params = "day")
  public Set<CalendarEvent> getAllCalendarEventsByDay(
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day
  ) {
    var range = localDateConverter.convertExternalLocalDate(
      userDetails.getUser().getSettings().getTimeZone(),
      day
    );
    return calendarEventService.getAllUserCalendarEventsByDay(userDetails.getUser(), range.getStart(), range.getEnd());
  }

  @GetMapping()
  public Set<CalendarEvent> getAllCalendarEvents(
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    return calendarEventService.getAllUserCalendarEvents(userDetails.getUser());
  }

  @PostMapping()
  public CalendarEvent createCalendarEvent(
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody CreateCalendarEventRequest createCalendarEventRequest
  ) throws ConflictingEventsException, UnableToResolveUsersByEmailsException {
    return calendarEventService.createCalendarEvent(
      userDetails.getUser(),
      createCalendarEventRequest
    );
  }
}
