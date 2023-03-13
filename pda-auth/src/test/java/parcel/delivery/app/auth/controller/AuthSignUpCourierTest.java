package parcel.delivery.app.auth.controller;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.api.models.request.SignInRequest;
import parcel.delivery.app.auth.api.models.request.SignUpRequest;
import parcel.delivery.app.auth.api.models.response.SignInResponse;
import parcel.delivery.app.auth.controller.client.ApiRestClient;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.security.core.UserType;
import parcel.delivery.app.auth.service.AuthenticationService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class AuthSignUpCourierTest {
    public static final SignUpRequest COURIER_ACC = SignUpRequest.builder()
            .clientId("test@mail.com")
            .firstName("John")
            .lastName("Doe")
            .phone("+741234341421")
            .password("12345678")
            .build();
    private static final String URL = "/auth/signup/courier";
    @Autowired
    private ApiRestClient client;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;


    @Test
    @DisplayName("/auth/courier/signup Should be unavailable for ROLE_USER")
    @WithMockUser
    void testUnavailabilityForNonAdminUser() throws Exception {
        client.post(URL, COURIER_ACC)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @DisplayName("/auth/courier/signup Should be available for user with authority CREATE_COURIER_USER")
    @WithMockUser(authorities = {"CREATE_COURIER_USER"})
    void testAvailabilityForCorrectAuthority() throws Exception {
        client.post(URL, COURIER_ACC)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("/auth/signup Should create courier user via admin account")
    void testCourierSignupWorkflow() throws Exception {
        // Setup admin account
        SignUpRequest adminSignUp = SignUpRequest.builder()
                .clientId("admin@mail.com")
                .firstName("Jane")
                .lastName("Doe")
                .phone("+39612321312")
                .password("234234123412")
                .build();
        authenticationService.signUp(adminSignUp, UserType.ADMIN);
        SignInRequest adminSignin = new SignInRequest(adminSignUp.clientId(), adminSignUp.password());
        SignInResponse signInResponse = authenticationService.signIn(adminSignin);
        // Courier acc
        client.post(URL, signInResponse.accessToken(), COURIER_ACC)
                .andExpect(status().isNoContent());

    }

    @AfterEach
    @Transactional
    public void cleanup() {
        userRepository.deleteAll();
    }
}
