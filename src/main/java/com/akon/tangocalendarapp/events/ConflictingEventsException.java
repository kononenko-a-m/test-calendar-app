package com.akon.tangocalendarapp.events;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

@ResponseStatus(value=HttpStatus.CONFLICT)
public class ConflictingEventsException extends Exception {
  public ConflictingEventsException(Collection<CalendarEvent> conflictingEvents) {
    super("There are conflicting events found " +
      conflictingEvents.stream()
        .map(e -> e.getId().toString())
        .collect(joining( ", "))
    );
  }
}
