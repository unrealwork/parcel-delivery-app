package parcel.delivery.app.auth.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.controller.api.response.SignInResponse;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.service.AuthenticationService;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.test.client.ApiRestClient;
import parcel.delivery.app.common.test.controller.BaseControllerTest;
import parcel.delivery.app.common.test.messaging.Sink;
import parcel.delivery.app.common.test.security.annotations.WithAdminRole;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.function.definition=userCreatedSink",
                "spring.cloud.stream.bindings.userCreatedSink-in-0.destination=user-created"
        })
class AuthSignUpCourierTest extends BaseControllerTest {
    public static final SignUpRequest COURIER_ACC = SignUpRequest.builder()
            .clientId(WithCourierRole.USERNAME)
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

    @SpyBean
    private Sink<SignedUpEvent> eventSink;


    @Test
    @DisplayName("/auth/courier/signup Should not be unavailable for ROLE_USER")
    @WithUserRole
    void testUnavailabilityForNonAdminUser() throws Exception {
        client.post(COURIER_ACC, URL)
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @DisplayName("/auth/courier/signup Should be available for user with authority CREATE_COURIER_USER")
    @WithAdminRole
    void testAvailabilityForCorrectAuthority() throws Exception {
        client.post(COURIER_ACC, URL)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("/auth/signup Should create courier user via admin account")
    void testCourierSignupWorkflow() throws Exception {
        // Setup admin account
        SignUpRequest adminSignUp = SignUpRequest.builder()
                .clientId(WithAdminRole.USERNAME)
                .firstName("Jane")
                .lastName("Doe")
                .phone("+39612321312")
                .password("234234123412")
                .build();
        authenticationService.signUp(adminSignUp, UserRole.ADMIN);
        SignInRequest adminSignin = new SignInRequest(adminSignUp.clientId(), adminSignUp.password());
        SignInResponse signInResponse = authenticationService.signIn(adminSignin);
        // Courier acc
        client.post(COURIER_ACC, signInResponse.accessToken(), URL)
                .andExpect(status().isNoContent());
        Mockito.verify(eventSink, Mockito.timeout(5000))
                .accept(new SignedUpEvent(UserRole.COURIER, WithCourierRole.USERNAME));
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        userRepository.deleteAll();
    }
}
