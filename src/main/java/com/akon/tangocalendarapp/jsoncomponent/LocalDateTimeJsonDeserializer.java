package com.akon.tangocalendarapp.jsoncomponent;

import com.akon.tangocalendarapp.timezone.TimeZone;
import com.akon.tangocalendarapp.users.User;
import com.akon.tangocalendarapp.users.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.TimeZone.getDefault;

@JsonComponent
public class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {
  @Value("${spring.jackson.date-format}")
  private String dateFormat;


  @Override
  public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    var textNode = (TextNode) jsonParser.getCodec().readTree(jsonParser);
    var dateTime = textNode.asText();
    var dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    var systemZoneId = ZoneId.of(getDefault().getID());

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userZoneId = systemZoneId;

    if (authentication != null) {
      var user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
      var timeZone = user.getSettings().getTimeZone();
      userZoneId = ZoneId.of(timeZone.getCode());
    }

    var externalDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter).atZone(userZoneId);

    return externalDateTime.withZoneSameInstant(systemZoneId).toLocalDateTime();
  }
}
