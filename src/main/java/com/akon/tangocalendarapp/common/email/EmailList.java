package com.akon.tangocalendarapp.common.email;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
  ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailListValidator.class)
@Documented
public @interface EmailList {
  String message() default "Invalid emails list";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
