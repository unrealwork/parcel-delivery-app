package parcel.delivery.app.common.swagger.swagger;

import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.security.core.RolePrivilege;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
class AuthPolicyCustomizerTest {
    public static final String TEST_DESC = "test";
    private final AuthPolicyCustomizer authPolicyCustomizer = new AuthPolicyCustomizer();
    private final Operation operation = new Operation();
    @Mock
    private HandlerMethod handlerMethod;
    @Mock
    private AuthPolicy authPolicy;

    @BeforeEach
    void setup() {
        operation.setDescription(TEST_DESC);
        Mockito.when(handlerMethod.hasMethodAnnotation(AuthPolicy.class))
                .thenReturn(true);
        Mockito.when(handlerMethod.getMethodAnnotation(AuthPolicy.class))
                .thenReturn(authPolicy);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            BASIC,false
            CANCEL_ORDER,true
            """)
    @DisplayName("Should add information about required privilege when annotation is present")
    void testCustomizationOfOperation(RolePrivilege rolePrivilege, boolean isPrivilegeInfoAdded) {
        Mockito.when(authPolicy.value())
                .thenReturn(rolePrivilege);
        authPolicyCustomizer.customize(operation, handlerMethod);
        assertThat(!TEST_DESC.equals(operation.getDescription()), equalTo(isPrivilegeInfoAdded));
    }

    @Test
    @DisplayName("Should add nothing when annotation is not present")
    void testMethodWithoutAnnotation() {
        Mockito.when(handlerMethod.hasMethodAnnotation(AuthPolicy.class))
                .thenReturn(false);
        Mockito.when(handlerMethod.getMethodAnnotation(AuthPolicy.class))
                .thenReturn(null);
        authPolicyCustomizer.customize(operation, handlerMethod);
        assertThat(operation.getDescription(), equalTo(TEST_DESC));
    }
}
