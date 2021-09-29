package com.akon.tangocalendarapp.rooms;

import com.akon.tangocalendarapp.users.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.UUID;

public interface ConferenceRoomRepository extends CrudRepository<ConferenceRoom, UUID> {
  Set<ConferenceRoom> findByManager(User manager);
}
