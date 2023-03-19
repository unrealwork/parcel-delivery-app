package parcel.delivery.app.common.annotations;


import parcel.delivery.app.common.security.core.RolePrivilege;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark specify authorization policy in controllers.
 * <p>
 * If annotation is specified without param it means that authenticated principal
 * should have only {@link RolePrivilege#BASIC} privilege.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AuthPolicy {
    /**
     * Allows access to controller method only if
     * user has specified {@link RolePrivilege}
     *
     * @return speified {@link RolePrivilege}
     */
    RolePrivilege value() default RolePrivilege.BASIC;
}
