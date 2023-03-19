package parcel.delivery.app.common.test.security.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@WithMockUser(username = WithCourierRole.USERNAME,
        authorities = {"ROLE_COURIER", "BASIC", "VIEW_ORDERS", "CHANGE_ORDER_STATUS"})
public @interface WithCourierRole {
    String USERNAME = "jack@doe.com";
}
