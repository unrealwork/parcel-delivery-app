package parcel.delivery.app.common.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.common.security.jwt.JwtProvider;
import parcel.delivery.app.common.security.jwt.JwtToken;
import parcel.delivery.app.common.util.WebUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_COURIER_USER;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private JwtProvider jwtProvider;

    public static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(new Object[] {null}),
                Arguments.of("Bear"),
                Arguments.of("Bearer invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Should not set auth for incorrect headers")
    void testIncorrectAuthorizationHeader(String authorizationHeader) throws ServletException, IOException {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider);
        Mockito.when(request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION))
                .thenReturn(authorizationHeader);

        filter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext()
                .getAuthentication());
    }

    @Test
    @DisplayName("Should set correct authentication data for valid token")
    void testValidToken() throws ServletException, IOException {
        String token = "valid";
        JwtToken jwtToken = JwtToken.builder()
                .clientId("test@gmail.com")
                .userType(UserType.ADMIN)
                .privileges(Collections.singletonList(CREATE_COURIER_USER))
                .build();

        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(WebUtil.bearerHeader(token));
        Mockito.when(jwtProvider.validate(token))
                .thenReturn(true);
        Mockito.when(jwtProvider.parse(token))
                .thenReturn(jwtToken);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider);
        filter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        assertEquals(jwtToken.clientId(), authentication.getPrincipal());
        assertEquals(jwtToken.authorities(), authentication.getAuthorities());
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(request, jwtProvider);
        SecurityContextHolder.getContext()
                .setAuthentication(null);
    }
}
