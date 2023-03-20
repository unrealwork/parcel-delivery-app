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
@WithMockUser(username = WithAdminRole.USERNAME,
        authorities = {"ROLE_ADMIN", "BASIC", "CREATE_COURIER_USER", "CHANGE_ORDER_STATUS", "VIEW_ORDERS",
                "ASSIGN_COURIER", "VIEW_COURIERS", "TRACK_DELIVERY"})
public @interface WithAdminRole {
    String USERNAME = "jane@doe.com";
}
