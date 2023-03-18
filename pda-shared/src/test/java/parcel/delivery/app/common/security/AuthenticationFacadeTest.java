package parcel.delivery.app.common.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_ORDER_STATUS;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_ORDERS;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthenticationFacadeTest {
    private static final String USER = "john@doe.com";

    @Autowired
    private AuthenticationFacade auth;

    @WithMockUser
    @DisplayName("Should provide access to current authentication")
    void authentication() {
        assertThat(auth.authentication(), notNullValue());
    }

    @Test
    @WithMockUser(username = USER)
    @DisplayName("Should provide access to authenticated principal username")
    void username() {
        assertThat(auth.username(), equalTo(USER));
    }

    @Test
    @WithMockUser(username = USER)
    @DisplayName("Should provide access to JWT data")
    void jwtToken() {
        assertThat(auth.jwtToken(), notNullValue());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Should provide access to user role")
    void role() {
        assertThat(auth.role(), equalTo(UserType.ADMIN));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "VIEW_ORDERS", "CHANGE_ORDER_STATUS"})
    @DisplayName("Should provide access to user privileges")
    void privileges() {
        Collection<RolePrivilege> privileges = auth.privileges();
        assertThat(privileges, hasSize(2));
        assertThat(privileges, containsInAnyOrder(VIEW_ORDERS, CHANGE_ORDER_STATUS));
    }

    @Configuration
    @EnableWebSecurity
    @Import( {AuthenticationFacade.class})
    static class Config {
    }
}
