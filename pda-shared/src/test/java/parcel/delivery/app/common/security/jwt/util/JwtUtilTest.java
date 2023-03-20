package parcel.delivery.app.common.security.jwt.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.security.jwt.JwtToken;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    public static Stream<Arguments> badCredentialsTestCases() {
        return Stream.of(
                Arguments.arguments(Set.of("ROLE_HACKER")),
                Arguments.arguments(Set.of("HACKER")),
                Arguments.arguments(Set.of("ROLE_USER","ROLE_ADMIN"))
        );
    }

    @Test
    @DisplayName("Parsing of valid authentication should return valid JWT")
    void testTokenFromAuthentication() {
        JwtToken jwtToken = JwtToken.builder()
                .clientId("test@mail.com")
                .privileges(Set.of(RolePrivilege.values()))
                .userRole(UserRole.ADMIN)
                .build();
        String principal = "test@mail.com";
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", jwtToken.authorities());
        assertEquals(jwtToken, JwtUtil.tokenFromAuthentication(authentication));
    }

    @ParameterizedTest
    @MethodSource("badCredentialsTestCases")
    @DisplayName("Invalid roles/privileges should recognized as Bad credentials")
    void testBadCredentials(Set<String> auhorities) {
        Set<GrantedAuthority> authorities = auhorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "", authorities);
        assertThrows(BadCredentialsException.class, () -> JwtUtil.tokenFromAuthentication(authentication));
    }
}
