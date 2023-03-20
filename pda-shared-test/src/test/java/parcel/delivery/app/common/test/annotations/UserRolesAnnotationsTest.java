package parcel.delivery.app.common.test.annotations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserRolesAnnotationsTest {
    @Autowired
    private AuthenticationFacade auth;

    @WithAdminRole
    @Test
    @DisplayName("Should provide same authentication for tests as role ADMIN")
    void testAdminRole() {
        assertThat(auth.authentication(), notNullValue());
        assertThat(auth.role(), equalTo(UserRole.ADMIN));
        assertThat(auth.privileges(), equalTo(UserRole.ADMIN.priviliges()));
    }

    @WithUserRole
    @Test
    @DisplayName("Should provide same authentication for tests as role USER")
    void testUserRole() {
        assertThat(auth.authentication(), notNullValue());
        assertThat(auth.role(), equalTo(UserRole.USER));
        assertThat(auth.privileges(), equalTo(UserRole.USER.priviliges()));
    }

    @WithCourierRole
    @Test
    @DisplayName("Should provide same authentication for tests as role COURIER")
    void testCourierRole() {
        assertThat(auth.authentication(), notNullValue());
        assertThat(auth.role(), equalTo(UserRole.COURIER));
        assertThat(auth.privileges(), equalTo(UserRole.COURIER.priviliges()));
    }

    @Configuration
    @EnableWebSecurity
    @Import(AuthenticationFacade.class)
    static class Config {
    }
}
