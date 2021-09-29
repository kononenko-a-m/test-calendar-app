package com.akon.tangocalendarapp.users;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Set<User> resolveUsersByEmails(Collection<String> emails) throws UnableToResolveUsersByEmailsException {
    var users = userRepository.findAllByEmails(emails);

    if (users.size() != emails.size()) {
      throw new UnableToResolveUsersByEmailsException(emails);
    }

    return users;
  }
}
