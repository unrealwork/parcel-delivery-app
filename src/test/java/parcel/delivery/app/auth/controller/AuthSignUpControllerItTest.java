package parcel.delivery.app.auth.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.api.models.request.SignUpRequest;
import parcel.delivery.app.auth.controller.client.ApiRestClient;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.service.AuthenticationService;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthSignUpControllerItTest {
    private static final String URL = "/auth/signup";

    @Autowired
    private ApiRestClient apiRestClient;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("/auth/signup should be available for anonymous user")
    @WithAnonymousUser
    void testSignupAvailability() throws Exception {
        final SignUpRequest requestDto = new SignUpRequest(
                "test@mail.com",
                "John",
                "Doe",
                "+123456789",
                "password"
        );
        apiRestClient.post(URL, requestDto)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("/auth/signup should reject already registered clientId")
    @Test
    void testSignUpWithAlreadyRegisteredClientId() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest
                .builder()
                .clientId("existing@user.com")
                .firstName("John")
                .lastName("Doe")
                .phone("+12345678")
                .password("12345678")
                .build();
        authenticationService.signUp(signUpRequest);
        apiRestClient.post(URL, signUpRequest)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value(containsStringIgnoringCase("already")));
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        userRepository.deleteAll();
    }
}
