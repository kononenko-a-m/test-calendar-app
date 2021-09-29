package com.akon.tangocalendarapp.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  @Query("FROM User u WHERE u.email IN (:emails)")
  Set<User> findAllByEmails(@Param("emails") Collection<String> emails);
}
