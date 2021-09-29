package com.akon.tangocalendarapp;

import com.akon.tangocalendarapp.env.AbstractIntegrationTest;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CalendarEventTest extends AbstractIntegrationTest {

  private final String JOHN_EMAIL = "john@tango.com";
  private final String JOHN_PASSWORD = "mock-password";

  private final String JENNY_EMAIL = "jenny@tango.com";
  private final String JENNY_PASSWORD = "mock-password";

  private String originalStart = tomorrowTime(9, 10);
  private String originalEnd = tomorrowTime(12, 10);

  private final String dateFormat = "yyyy-MM-dd HH:mm:ss";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  public void shouldCreateEvent() throws Exception {
    var event = createEvent(
      new JSONObject()
        .put("eventName", "Java Conf")
        .put("meetingAgenda", "Talk about cool features")
        .put("start", originalStart)
        .put("end", originalEnd)
        .put("participants", new JSONArray().put(JENNY_EMAIL))
    );

    assertThatJson(event)
      .and(
        JsonAssert::isObject,
        e -> e.node("eventName").isEqualTo("Java Conf"),
        e -> e.node("meetingAgenda").isEqualTo("Talk about cool features"),
        e -> e.node("start").isString().endsWith("09:10:00").isEqualTo(originalStart),
        e -> e.node("end").isString().endsWith("12:10:00").isEqualTo(originalEnd),
        e -> e.node("owner.email").isString().isEqualTo(JOHN_EMAIL)
      );

    assertThatJson(event)
      .inPath("participants[*].email")
      .isArray()
      .containsAnyOf(JENNY_EMAIL);
  }

  @Test
  @Order(2)
  public void shouldGetAllEventsWithTZSupport() throws Exception {
    var originalEvents = restTemplate.withBasicAuth(JENNY_EMAIL, JENNY_PASSWORD)
      .getForObject(route("/calendar-events"), String.class);

    assertThatJson(originalEvents)
      .and(
        es -> es.isArray().hasSize(1),
        es -> es.node("[0].start").isString().isNotEqualTo(originalStart),
        es -> es.node("[0].end").isString().isNotEqualTo(originalEnd)
      );

    var newStart = tomorrowTime(13, 40);
    var newEnd = tomorrowTime(16, 40);

    createEvent(
      new JSONObject()
        .put("eventName", "Community Conf")
        .put("meetingAgenda", "Coffee and beverages for FREE")
        .put("start", newStart)
        .put("end", newEnd)
        .put("participants", new JSONArray().put(JENNY_EMAIL))
    );

    var changedEvents = getAllEvents();

    assertThatJson(changedEvents)
      .inPath("[*].eventName")
      .isArray()
      .containsAnyOf("Community Conf");

    assertThatJson(changedEvents)
      .inPath("[*].meetingAgenda")
      .isArray()
      .containsAnyOf("Coffee and beverages for FREE");

    assertThatJson(changedEvents)
      .inPath("[*].start")
      .isArray()
      .containsAnyOf(newStart, originalStart);

    assertThatJson(changedEvents)
      .inPath("[*].end")
      .isArray()
      .containsAnyOf(newEnd, originalEnd);
  }

  @Test
  @Order(3)
  public void shouldGetEventsByQuery() {
    var events = restTemplate.withBasicAuth(JOHN_EMAIL, JOHN_PASSWORD)
      .getForObject(route("/calendar-events") + "?query=Java", String.class);

    assertThatJson(events)
      .inPath("[*].eventName")
      .isArray()
      .hasSize(1)
      .containsAnyOf("Java Conf");
  }

  private String tomorrowTime(int hours, int minutes) {
    return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(hours, minutes))
      .format(DateTimeFormatter.ofPattern(dateFormat));
  }

  private String getAllEvents() {
    return restTemplate.withBasicAuth(JOHN_EMAIL, JOHN_PASSWORD)
      .getForObject(route("/calendar-events"), String.class);
  }

  private String createEvent(JSONObject event) {
    return restTemplate.withBasicAuth(JOHN_EMAIL, JOHN_PASSWORD)
      .exchange(createEventRequest(event), String.class)
      .getBody();
  }

  private RequestEntity<String> createEventRequest(JSONObject room) {
    return RequestEntity.post(route("/calendar-events"))
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .body(room.toString());
  }

  private String route(String subRoute) {
    return "http://localhost:" + port + subRoute;
  }
}
