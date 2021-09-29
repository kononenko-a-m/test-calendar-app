INSERT INTO users (id, name, email, company_id) VALUES
(
   UUID_TO_BIN(UUID()),
   'John',
   'john@tango.com',
   UUID_TO_BIN('13fd4269-be2a-4683-b13d-84dbccbcd020')
 ),
(
   UUID_TO_BIN(UUID()),
   'Jenny',
   'jenny@tango.com',
   UUID_TO_BIN('13fd4269-be2a-4683-b13d-84dbccbcd020')
 );

INSERT INTO user_settings (id, user_id, timezone_id) VALUES
(
  (UUID_TO_BIN(UUID())),
  (SELECT id FROM users WHERE email = 'john@tango.com'),
  (SELECT id FROM timezones WHERE code = 'Europe/London')
),
(
  (UUID_TO_BIN(UUID())),
  (SELECT id FROM users WHERE email = 'jenny@tango.com'),
  (SELECT id FROM timezones WHERE code = 'America/New_York')
);
