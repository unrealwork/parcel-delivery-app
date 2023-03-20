package parcel.delivery.app.common.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.config.properties.JwtProviderProperties;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@Slf4j
@EnableConfigurationProperties(value = JwtProviderProperties.class)
@SpringBootTest
class JwtProviderTest {
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("Test that generated token could be parsed")
    void testParseGenerate() {
        Set<RolePrivilege> privileges = Set.of(RolePrivilege.CREATE_COURIER_USER);
        final JwtToken jwtToken = JwtToken.builder()
                .clientId("test@mail.com")
                .privileges(privileges)
                .userType(UserRole.ADMIN)
                .build();
        String generatedToken = jwtProvider.generate(jwtToken);
        JwtToken parsedToken = jwtProvider.parse(generatedToken);
        assertEquals(parsedToken.clientId(), jwtToken.clientId());
        assertEquals(privileges, parsedToken.privileges());
        Assertions.assertEquals(UserRole.ADMIN, parsedToken.userRole());
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
    
    @Configuration
    public static class Config {
        @Bean
        public JwtProvider jwtProvider(JwtProviderProperties providerProperties) {
            return new JwtProviderImpl(providerProperties);
        }
    }
}
