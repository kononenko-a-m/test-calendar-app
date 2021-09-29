package com.akon.tangocalendarapp;

import com.akon.tangocalendarapp.env.AbstractIntegrationTest;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import org.json.JSONException;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConferenceRoomTest extends AbstractIntegrationTest {

  private final String JOHN_EMAIL = "john@tango.com";
  private final String JOHN_PASSWORD = "mock-password";

  private String roomName = "Under the Stairs";
  private String address = "4 Privet Drive, Little";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  public void shouldCreateRoom() throws Exception {
    var room = createRoom(buildRoomJson(roomName, address));

    assertThatJson(room)
      .and(
        JsonAssert::isObject,
        r -> r.node("name").isEqualTo(roomName),
        r -> r.node("address").isEqualTo(address),
        r -> r.node("manager.email").isEqualTo(JOHN_EMAIL)
      );
  }

  // this test dependent on the previous one
  @Test
  @Order(2)
  public void shouldFetchAllRooms() throws Exception {
    var newRoom = "Hogwart";
    var newAddress = "Hogsmead";
    var initialRooms = getAllRooms();

    assertThatJson(initialRooms)
      .isArray()
      .hasSize(1);

    createRoom(buildRoomJson(newRoom, newAddress));
    var changedRooms = getAllRooms();

    assertThatJson(changedRooms)
      .inPath("[*].name")
      .isArray()
        .containsAnyOf("Hogwart", roomName, "1");
  }

  private String getAllRooms() {
    return restTemplate.withBasicAuth(JOHN_EMAIL, JOHN_PASSWORD)
      .getForObject(route("/conference-rooms"), String.class);
  }

  private String createRoom(JSONObject room) {
    var request = RequestEntity.post(route("/conference-rooms"))
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .body(room.toString());
    return restTemplate.withBasicAuth(JOHN_EMAIL, JOHN_PASSWORD)
      .exchange(request, String.class)
      .getBody();
  }

  private JSONObject buildRoomJson(String name, String address) throws JSONException {
    var roomJson = new JSONObject();
    roomJson.put("name", name);
    roomJson.put("address", address);
    return roomJson;
  }

  private String route(String subRoute) {
    return "http://localhost:" + port + subRoute;
  }
}
