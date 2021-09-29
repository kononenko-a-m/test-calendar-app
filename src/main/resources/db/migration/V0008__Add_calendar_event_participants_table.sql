CREATE TABLE calendar_event_participants (
  calendar_event_id BINARY(16) NOT NULL,
  participant_id BINARY(16) NOT NULL,

  FOREIGN KEY (calendar_event_id) REFERENCES calendar_events(id),
  FOREIGN KEY (participant_id) REFERENCES users(id)
) ENGINE=InnoDB;
