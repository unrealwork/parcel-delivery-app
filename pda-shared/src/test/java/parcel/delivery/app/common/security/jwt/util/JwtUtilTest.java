package parcel.delivery.app.common.security.jwt.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.common.security.jwt.JwtToken;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilTest {

    @Test
    @DisplayName("Parsing of valid authentication should return valid JWT")
    void tokenFromAuthentication() {
        JwtToken jwtToken = JwtToken.builder()
                .clientId("test@mail.com")
                .privileges(Set.of(RolePrivilege.values()))
                .userType(UserType.ADMIN)
                .build();
        String principal = "test@mail.com";
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", jwtToken.authorities());
        assertEquals(jwtToken, JwtUtil.tokenFromAuthentication(authentication));
    }
}
