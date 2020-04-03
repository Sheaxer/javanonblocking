package stuba.fei.gono.java.validation.annotations;

import stuba.fei.gono.java.validation.BankingDayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * Annotation for validation o
 */
@Target(ElementType.FIELD)
@Constraint(validatedBy = BankingDayValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface BankingDay {
    String message() default "";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
