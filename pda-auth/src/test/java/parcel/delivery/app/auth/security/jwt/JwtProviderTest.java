package parcel.delivery.app.auth.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import parcel.delivery.app.auth.security.core.RolePrivilege;
import parcel.delivery.app.auth.security.core.UserType;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@SpringBootTest
class JwtProviderTest {
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("Test that generated token could be parsed")
    void testParseGenerate() {
        List<RolePrivilege> privileges = List.of(RolePrivilege.CREATE_COURIER_USER);
        final JwtToken jwtToken = JwtToken.builder()
                .clientId("test@mail.com")
                .privileges(privileges)
                .userType(UserType.ADMIN)
                .build();
        String generatedToken = jwtProvider.generate(jwtToken);
        JwtToken parsedToken = jwtProvider.parse(generatedToken);
        assertEquals(parsedToken.clientId(), jwtToken.clientId());
        assertEquals(privileges, parsedToken.privileges());
        assertEquals(UserType.ADMIN, parsedToken.userType());
    }

    @Test
    @DisplayName("Should discard non valid token")
    void validate() {
        JwtToken jwtToken = JwtToken.builder()
                .issuedAt(Instant.EPOCH)
                .build();
        String token = jwtProvider.generate(jwtToken);
        boolean isValid = jwtProvider.validate(token);
        assertFalse(isValid);
    }
}
