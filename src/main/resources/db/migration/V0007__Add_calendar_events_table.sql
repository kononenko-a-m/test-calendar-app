CREATE TABLE calendar_events (
  id BINARY(16) NOT NULL PRIMARY KEY,
  owner_id BINARY(16) NOT NULL,
  event_name VARCHAR(255) NOT NULL,
  meeting_agenda TEXT NOT NULL,
  start DATETIME NOT NULL,
  end DATETIME NOT NULL,
  location_id BINARY(16),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT start_end_range_constraint CHECK(start < end),

  -- this would be used for gap locking
  INDEX schedule_index (location_id, start, end),

  -- this index would be used for searching events by day
  INDEX day_events_index(end, start),

  -- this index would be used for searching by query
  FULLTEXT query_index(event_name, meeting_agenda),

  FOREIGN KEY (location_id) REFERENCES conference_rooms(id),
  FOREIGN KEY (owner_id) REFERENCES users(id)
) ENGINE=InnoDb;
