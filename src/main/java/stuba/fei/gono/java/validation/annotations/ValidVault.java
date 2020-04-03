package stuba.fei.gono.java.validation.annotations;

import stuba.fei.gono.java.validation.VaultValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = VaultValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVault {

    String message() default "VAULT_ERROR";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
