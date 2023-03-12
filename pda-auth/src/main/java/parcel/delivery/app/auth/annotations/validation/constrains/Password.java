package parcel.delivery.app.auth.annotations.validation.constrains;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Constraint validation for users password
 */
@Target( {FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@NotNull
@Size(min = 8, max = 20, message = "{validation.constraint.passwordLen}")
@Constraint(validatedBy = {})
@Documented
public @interface Password {
    String message() default "Incorrect password";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
