package com.akon.tangocalendarapp.rooms;

import com.akon.tangocalendarapp.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ConferenceRoomService {

  private final ConferenceRoomRepository conferenceRoomRepository;

  ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository) {
    this.conferenceRoomRepository = conferenceRoomRepository;
  }

  public ConferenceRoom createRoom(User manager, CreateConferenceRoomRequest createConferenceRoomRequest) {
    var conferenceRoom = new ConferenceRoom();

    conferenceRoom.setManager(manager);
    conferenceRoom.setName(createConferenceRoomRequest.getName());
    conferenceRoom.setAddress(createConferenceRoomRequest.getAddress());

    conferenceRoomRepository.save(conferenceRoom);

    return conferenceRoom;
  }

  public Set<ConferenceRoom> getAllRoomsByManager(User manager) {
    return conferenceRoomRepository.findByManager(manager);
  }

  public Optional<ConferenceRoom> getRoomById(UUID id) {
    return conferenceRoomRepository.findById(id);
  }
}
