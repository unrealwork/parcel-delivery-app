package parcel.delivery.app.auth.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.service.AuthenticationService;
import parcel.delivery.app.common.test.client.ApiRestClient;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthSignInControllerTest {
    private static final String URL = "/auth/signin";

    @Autowired
    private ApiRestClient client;
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;


    @ParameterizedTest
    @CsvSource( {
            "a,b,400",
            "test@mail.com,12345678,401"
    })
    @DisplayName("/api/signin Should return correct status for non valid requests")
    void testNotValidReq(String clientId, String secretKey, int status) throws Exception {
        SignInRequest requestDto = new SignInRequest(clientId, secretKey);
        signInRequest(requestDto)
                .andDo(print())
                .andExpect(status().is(status));

    }


    @Test
    @DisplayName("Verify successful authentication")
    void testValidReq() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId("test@mail.com")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .phone("+7123423423")
                .build();
        authenticationService.signUp(signUpRequest);
        signInRequest(new SignInRequest(signUpRequest.clientId(), signUpRequest.password()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    private ResultActions signInRequest(SignInRequest requestDto) throws Exception {
        return client.post(requestDto, URL);
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        userRepository.deleteAll();
    }
}
