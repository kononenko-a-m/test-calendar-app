package com.akon.tangocalendarapp.common.email;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class EmailListValidator implements ConstraintValidator<EmailList, Collection<String>> {

  private final EmailValidator emailValidator = new EmailValidator();

  @Override
  public void initialize(EmailList constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    return value.stream().allMatch(probablyEmail -> emailValidator.isValid(probablyEmail, context));
  }
}
