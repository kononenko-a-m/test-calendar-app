package com.akon.tangocalendarapp.events;

import com.akon.tangocalendarapp.users.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
  FROM CalendarEvent ce
  WHERE
    ce.location.id = :locationId
    AND ce.start >= :start
    AND ce.end <= :end
  """)
  Set<CalendarEvent> findCalendarEventRangeWithLock(
    @Param("locationId") UUID locationId,
    @Param("start") LocalDateTime start,
    @Param("end") LocalDateTime end
  );

  @Query("""
    FROM CalendarEvent ce
    JOIN ce.participants p
    LEFT JOIN ce.location l
    WHERE
      :user IN(p)
      OR l.manager = :user
  """)
  Set<CalendarEvent> findVisibleForUserCalendarEvents(
    @Param("user") User user
  );

  @Query(
    value = """
    SELECT
      *
    FROM
      `calendar_events` AS `ce`
    LEFT JOIN `calendar_event_participants` AS `cep`
      ON (`ce`.`id` = `cep`.`calendar_event_id`)
    LEFT JOIN `conference_rooms` AS `cr`
      ON `ce`.`location_id` = `cr`.`id`
    WHERE
      (
        `cr`.`manager_id` = :userId
        OR
        `cep`.`participant_id` = :userId
      )
      AND
      MATCH(`ce`.`event_name`, `ce`.`meeting_agenda`)
      AGAINST(:query)
    """,
    nativeQuery = true
  )
  Set<CalendarEvent> findByUserIdAndQuery(@Param("userId") UUID userId, @Param("query") String query);

  @Query(
    """
      FROM CalendarEvent ce
      JOIN ce.participants p
      LEFT JOIN ce.location l
      WHERE (
          :user IN(p)
          OR l.manager = :user
        )
        AND ce.location.id = :locationId
    """
  )
  Set<CalendarEvent> findByUserAndLocationId(
    @Param("user") User user,
    @Param("locationId") UUID locationId
  );

  @Query(
    """
      FROM CalendarEvent ce
      JOIN ce.participants p
      LEFT JOIN ce.location l
      WHERE (
          :user IN(p)
          OR l.manager = :user
        )
        AND (
          :start <= ce.end
          AND :end > ce.start
        )
    """
  )
  Set<CalendarEvent> findByRange(
    @Param("user") User user,
    @Param("start") LocalDateTime start,
    @Param("end") LocalDateTime end
  );
}
