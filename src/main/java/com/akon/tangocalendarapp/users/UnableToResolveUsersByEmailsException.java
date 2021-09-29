package com.akon.tangocalendarapp.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class UnableToResolveUsersByEmailsException extends Exception {
  UnableToResolveUsersByEmailsException(Collection<String> emails) {
    super("Unable to resolve users by emails " + String.join(", ", emails));
  }
}
