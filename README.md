# Tango Calendar App

## Requirements

- Docker

## How to run app

```bash
./gradlew build
docker compose -f ./docker-compose.db.yml -f ./docker-compose.yml up
```

## How to run test

```bash
./gradlew test
```

## API

Application expose following endpoints:

- `GET /conference-rooms` - return all rooms
- `POST /conference-rooms` - create a room, for payload details take a look at `com/akon/tangocalendarapp/rooms/CreateConferenceRoomRequest.java`
- `GET /calendar-events` - return all events; this endpoint support several query params for different filtering (`day`, `query` and `location_id`)
- `POST /calendar-events` - create an event, for payload details take a look at `com/akon/tangocalendarapp/events/CalendarEvent.java`

