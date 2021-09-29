package com.akon.tangocalendarapp.rooms;

import com.akon.tangocalendarapp.users.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController()
@RequestMapping("/conference-rooms")
public class ConferenceRoomController {

  private final ConferenceRoomService conferenceRoomService;

  ConferenceRoomController(ConferenceRoomService conferenceRoomService) {
    this.conferenceRoomService = conferenceRoomService;
  }

  @PostMapping()
  public ConferenceRoom createRoom(
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody CreateConferenceRoomRequest createConferenceRoomRequest
  ) {
    return conferenceRoomService.createRoom(
      userDetails.getUser(),
      createConferenceRoomRequest
    );
  }

  @GetMapping()
  public Set<ConferenceRoom> getAllConferenceRooms(
    @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    return conferenceRoomService.getAllRoomsByManager(userDetails.getUser());
  }
}
