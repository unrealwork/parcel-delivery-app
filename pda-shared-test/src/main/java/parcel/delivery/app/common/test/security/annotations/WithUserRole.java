package parcel.delivery.app.common.test.security.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author unrea
 */
@Target( {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@WithMockUser(username = WithUserRole.USERNAME, authorities = {"ROLE_USER", "BASIC", "VIEW_ORDERS", "CREATE_ORDER", "CANCEL_ORDER", "CHANGE_DESTINATION", "VIEW_DELIVERY_DETAILS"})
public @interface WithUserRole {
    String USERNAME = "john@doe.com";
}
