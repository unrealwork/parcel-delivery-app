package parcel.delivery.app.auth.service.init;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.service.AuthenticationService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static parcel.delivery.app.common.security.core.UserRole.COURIER;

@SpringBootTest(properties = {
        "application.users[0].username=jack@doe.com",
        "application.users[0].password=12345678",
        "application.users[0].firstName=Jack",
        "application.users[0].lastName=Doe",
        "application.users[0].role=COURIER",
})
@ExtendWith(SpringExtension.class)
class UserPropertiesInitServiceTest {
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Test init users registration via properties")
    void testInit() {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId("jack@doe.com")
                .password("12345678")
                .firstName("Jack")
                .lastName("Doe")
                .build();
        verify(authenticationService,
                times(1)).signUp(signUpRequest, COURIER);
    }
}
