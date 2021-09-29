package com.akon.tangocalendarapp.timezone;

import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.util.TimeZone.getDefault;

@Service
public class LocalDateConverter {
  private ZoneId systemZoneId = ZoneId.of(getDefault().getID());

  public LocalDateTimeRange convertExternalLocalDate(TimeZone externalTimeZone, LocalDate day) {
    var externalZoneId = ZoneId.of(externalTimeZone.getCode());

    var externalStart = day.atStartOfDay().atZone(externalZoneId);
    var externalEnd = externalStart.plus(1, ChronoUnit.DAYS);

    var internalStart = externalStart.withZoneSameInstant(systemZoneId).toLocalDateTime();
    var internalEnd = externalEnd.withZoneSameInstant(systemZoneId).toLocalDateTime();

    return new LocalDateTimeRange(internalStart, internalEnd);
  }

  public class LocalDateTimeRange {
    private final LocalDateTime start;
    private final LocalDateTime end;


    public LocalDateTimeRange(LocalDateTime start, LocalDateTime end) {
      this.start = start;
      this.end = end;
    }

    public LocalDateTime getStart() {
      return start;
    }

    public LocalDateTime getEnd() {
      return end;
    }
  }
}
