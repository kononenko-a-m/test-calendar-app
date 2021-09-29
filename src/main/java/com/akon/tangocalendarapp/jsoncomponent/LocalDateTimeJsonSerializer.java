package com.akon.tangocalendarapp.jsoncomponent;

import com.akon.tangocalendarapp.timezone.TimeZone;
import com.akon.tangocalendarapp.users.User;
import com.akon.tangocalendarapp.users.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.TimeZone.getDefault;

@JsonComponent
public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {
  @Value("${spring.jackson.date-format}")
  private String dateFormat;

  @Override
  public void serialize(LocalDateTime localDateTime, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    var systemZoneId = ZoneId.of(getDefault().getID());
    var internalDateTime = localDateTime.atZone(systemZoneId);

    var externalDateTime = internalDateTime;

    if (authentication != null) {
      var user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
      var timeZone = user.getSettings().getTimeZone();

      externalDateTime = internalDateTime.withZoneSameInstant(ZoneId.of(timeZone.getCode()));
    }

    gen.writeString(externalDateTime.format(dateTimeFormatter));
  }
}
