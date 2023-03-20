package parcel.delivery.app.auth.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.controller.api.response.SignInResponse;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.service.AuthenticationService;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.test.client.ApiRestClient;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthMeTestControllerTest {

    private static final String URL = "/auth/me";

    @Autowired
    private ApiRestClient client;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("/auth/me should be accessible with generated JWT token for existing user")
    void testAccessWithGeneratedToken() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId("test@mail.com")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .phone("+7123423423")
                .build();

        authenticationService.signUp(signUpRequest);
        SignInRequest signInRequest = new SignInRequest(signUpRequest.clientId(), signUpRequest.password());
        SignInResponse result = authenticationService.signIn(signInRequest);
        client.get(URL, result.accessToken())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(equalTo(signUpRequest.clientId())))
                .andExpect(jsonPath("$.role").value(equalTo(UserRole.USER.name())));
    }

    @Test
    @DisplayName("/auth/me should not be available for anonymous user")
    void testUnavailabilityForAnon() throws Exception {
        client.get(URL)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("/auth/me should be available for authenticated user")
    @WithUserRole
    void testAvailabilityForAuthenticated() throws Exception {
        client.get(URL)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(WithUserRole.USERNAME));
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        userRepository.deleteAll();
    }
}
